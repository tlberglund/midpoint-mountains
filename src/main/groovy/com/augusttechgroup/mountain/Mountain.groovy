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
import com.augusttechgroup.mountain.*
m = new Mountain()
m.scaleFunction = { scale -> scale }
oldRow = [1,2]
m.displaceRow(oldRow)
  */
  def displaceRow(row) {
    def newRow = []
    for(int n = 0; n < row.size() - 1; n++) {
      def triple = createTripleFromPair(row[n..(n+1)], nextScale(row))
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
   * Inserts a newly created middle row in rows at the index immediately following
   * the given index.
   */
  def insertRowAfterIndex(index) {
    if(index >= (rows.size() - 1)) {
      throw new IllegalArgumentException("Cannot insert rows past index ${rows.size() - 1}")
    }
    rows = rows[0..index] + [createMiddleRow(rows[index], rows[index + 1])] + rows[(index + 1)..-1]

    return rows[index + 1]
  }


  def createMiddleRow(topRow, bottomRow) {
    if(topRow?.size() != bottomRow?.size()) {
      throw new IllegalArgumentException("Rows are not of the same size (${topRow?.size()} vs ${bottomRow?.size()})")
    }
    
    def scale = nextScale(topRow)
    def middleRow = []
    topRow.eachWithIndex { topPoint, index ->
      def bottomPoint = bottomRow[index]
      middleRow << midpointElevation([topPoint, bottomPoint], displacement.curry(scale))
    }
    
    return middleRow
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


  def grow() {
    rows.eachWithIndex { row, index ->
      rows[index] = displaceRow(row)
    }
    
    rows[0..-2].eachWithIndex { row, index -> 
      insertRowAfterIndex(index)
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

  
  String toString() {
    rows.toString()
  }
}

