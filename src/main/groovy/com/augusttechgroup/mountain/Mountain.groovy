//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

package com.augusttechgroup.mountain


/**
 *
 **/
class Mountain {

  Random random
  Point northWest
  Closure scaleFunction
  int scale
  
  
  Mountain() {
    random = new Random()
    random.setSeed(new Date().time)
    
    scale = 1
    scaleFunction = { scale ->
      random.nextGaussian() / Math.pow(2, scale)
    }
  }


  def initialize() {
    def displacer = scaleFunction.curry(scale)
    def range = buildInitialRange(displacer, scale)
    northWest = range[0][0]
    
    return range
  }
  

  def buildInitialRange(displacer = null, scale = 1) {
    def northWest = new Point(displacer: displacer, scale: scale)
    def northEast = new Point(displacer: displacer, scale: scale)
    def southEast = new Point(displacer: displacer, scale: scale)
    def southWest = new Point(displacer: displacer, scale: scale)
    
    northWest.east = northEast
    northWest.south = southWest
    
    northEast.west = northWest
    northEast.south = southEast
    
    southEast.north = northEast
    southEast.west = southWest
    
    southWest.north = northWest
    southWest.east = southEast
    
    return [[northWest, northEast], [southWest, southEast]]
  }
  
  
  def grow() {
    scale++

    northWest.eachSouth { point -> 
      doInsertionsOnEastWestRow(point, scale, scaleFunction.curry(scale))
    }
    
    northWest.eachSouth { point ->
      def southernPoint = point.south
      if(southernPoint) {
        insertBetweenRows(point, southernPoint, scale, scaleFunction.curry(scale))
      }
    }
  }
  
  
  def export(pw = System.out) {
    toArray().each { row -> 
      pw.println row.join('\t')
    }
  }


  def tweens(toMountain, tweenCount) {
    def toElevations = toMountain.toArray()
    def fromElevations = toArray()
    def deltaElevations = delta(toElevations)
    def mountains = []
    tweenCount.times { tween ->
      def rows = []
      fromElevations.eachWithIndex { fromRow, fromRowNumber ->
        def row = []
        fromRow.eachWithIndex { fromCol, fromColNumber ->
          row << fromCol + (deltaElevations[fromRowNumber][fromColNumber] * (tween + 1))
        }
        rows << row
      }
      mountains << rows
    }
    
    return mountains
  }
  

  def interpolate(Mountain toMountain) {
    interpolate(toMountain.toArray())
  }
  

  def delta(toMountainArray) {
    def fromRows = toArray()
    def toRows = toMountainArray
    def rows = []
    fromRows.eachWithIndex { fromRow, fromRowNumber ->
      def row = []
      fromRow.eachWithIndex { fromCol, fromColNumber ->
        def toCol = toRows[fromRowNumber][fromColNumber]
        row << toCol - fromCol
      }
      rows << row
    }
    
    return rows
  }
  

  def interpolate(toMountainArray, step = 2) {
    def fromRows = toArray()
    def toRows = toMountainArray
    def rows = []
    fromRows.eachWithIndex { fromRow, fromRowNumber ->
      def row = []
      fromRow.eachWithIndex { fromCol, fromColNumber ->
        def toCol = toRows[fromRowNumber][fromColNumber]
        row << ((toCol - fromCol) / step) + fromCol
      }
      rows << row
    }
    
    return rows
  }
  
  
  /**
   * Converts the elevation graph to a two-dimensional list of elevation values.
   */
  def toArray() {
    northWest.collectSouth { nsPoint ->
      nsPoint.collectEast { ewPoint -> ewPoint.elevation }
    }.toArray()
  }
  

  def doInsertionsOnEastWestRow(westernPoint, scale, displacer = null) {
    westernPoint.eachEast { point ->
      if(point.east) {
        def newPoint = point.insertEast(scale, displacer)
        newPoint.displace((point.elevation + newPoint.east.elevation) / 2)
      }
    }
  }
  

  def insertBetweenRows(northRow, southRow, scale, displacer = null) {
    def northIterator = northRow.eastIterator()
    def southIterator = southRow.eastIterator()
    def lastMiddlePoint
    def firstMiddlePoint
    
    while(northIterator.hasNext()) {
      def northPoint = northIterator?.next()
      def southPoint = southIterator?.next()
      
      def middlePoint = new Point()
      middlePoint.scale = scale
      middlePoint.displacer = displacer
      middlePoint.north = northPoint
      middlePoint.south = southPoint
      middlePoint.west = lastMiddlePoint
      if(lastMiddlePoint) lastMiddlePoint.east = middlePoint
      
      northPoint.south = middlePoint
      southPoint.north = middlePoint
      
      middlePoint.displace((northPoint.elevation + southPoint.elevation) / 2)
      
      lastMiddlePoint = middlePoint
      if(!firstMiddlePoint) firstMiddlePoint = middlePoint
    }
    
    return firstMiddlePoint
  }
}

