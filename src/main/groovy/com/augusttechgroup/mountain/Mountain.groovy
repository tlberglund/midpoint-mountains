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
    rows[0][0] = displacement(currentScale(rows[0]))
    rows[0][1] = displacement(currentScale(rows[0]))
    rows[1][0] = displacement(currentScale(rows[1]))
    rows[1][1] = displacement(currentScale(rows[1]))
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
    random.nextGaussian() / scale
  }
  
  
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
  
  
  def createMiddleRow(topRow, bottomRow) {
    if(topRow?.size() != bottomRow?.size()) {
      throw new IllegalArgumentException("Rows are not of the same size (${topRow?.size()} vs ${bottomRow?.size()})")
    }
    
    def scale = currentScale(topRow)
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

    def newRows = []
    rows[0..-2].eachWithIndex { row, index -> 
      newRows << rows[index]
      newRows << createMiddleRow(rows[index], rows[index + 1])
    }
    newRows << rows[-1]

    rows = newRows
  }


  def export(pw = System.out) {
    rows.each { row -> 
      pw.println row.join('\t')
    }
  }


  def tweens(toMountain, tweenCount) {
    def deltaElevations = delta(toMountain)
    def mountains = []
    tweenCount.times { tween ->
      def tweenRows = []
      rows.eachWithIndex { fromRow, fromRowNumber ->
        def tweenRow = []
        fromRow.eachWithIndex { fromCol, fromColNumber ->
          tweenRow << fromCol + (deltaElevations[fromRowNumber][fromColNumber] * ((double)tween / tweenCount))
        }
        tweenRows << tweenRow
      }
      mountains << tweenRows
    }
    
    return mountains
  }


  def delta(toMountain) {
    def deltaRows = []
    rows.eachWithIndex { fromRow, fromRowNumber ->
      def deltaRow = []
      fromRow.eachWithIndex { fromCol, fromColNumber ->
        def toCol = toMountain[fromRowNumber][fromColNumber]
        deltaRow << toCol - fromCol
      }
      deltaRows << deltaRow
    }
    
    return deltaRows
  }


  String toString() {
    rows.toString()
  }
}

