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

  def rows
  Random random

  
  Mountain() {
    random = new Random()
    random.setSeed(new Date().time)
    rows = [ [0,0], [0,0] ]
    rows[0][0] = displacement(nextScale(rows[0]))
    rows[0][1] = displacement(nextScale(rows[0]))
    rows[1][0] = displacement(nextScale(rows[1]))
    rows[1][1] = displacement(nextScale(rows[1]))
  }


  def size() {
    rows.size()
  }


  def getAt(row) {
    rows[row]
  }


  def currentScale(row) {
    row.size() - 1
  }


  def nextScale(row) {
    currentScale(row) * 2
  }


  def displacement = { scale ->
    scaleFunction(scale)
  }


  def scaleFunction = { scale ->
    random.nextGaussian() / (double)scale
  }
  
  
  /*
               a a
              a b a
            a c b c a
        a d c d b d c d a
a e d e c e d e b e d e c e d e a



import com.augusttechgroup.mountain.*
m = new Mountain()
m.scaleFunction = { scale -> scale }
oldRow = [1,2]
m.displaceRow(oldRow)

  */
  def displaceRow(row) {
    def newRow = []
    for(int n = 0; n < row.size() - 1; n++) {
      def pair = row[n..(n+1)]
      def ns = nextScale(row)
      def triple = createTripleFromPair(pair, ns)
      println "pair=${pair}"
      println "nextScale=${ns}"
      println "triple=${triple.getClass()}"
      println "newRole=${newRow.getClass()}"
      if(newRow) {
        newRow << triple[1..2]
      }
      else {
        newRow << triple
      }
      newRow = newRow.flatten()
    }
    
    return newRow
  }


  /**
   * Takes a pair of points and returns a triple, with a new point added
   * in the middle displaced from the average of the two according to the
   * prescribed scale.
   */
  def createTripleFromPair(pair, scale) {
    [pair[0], midpointElevation(pair, displacement.curry(scale)), pair[1]]
  }


  def midpointElevation = { pair, displacer ->
    ((pair[0] + pair[1]) / 2) + displacer()
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
    
//    northWest.displace()
//    northEast.displace()
//    southEast.displace()
//    southWest.displace()
    
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
          row << fromCol + (deltaElevations[fromRowNumber][fromColNumber] * ((double)tween / tweenCount))
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

