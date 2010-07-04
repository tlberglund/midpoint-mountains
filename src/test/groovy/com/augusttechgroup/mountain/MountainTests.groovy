
package com.augusttechgroup.mountain

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import static org.junit.Assert.*


class MountainTests {

  def mountain


  @Before
  void buildTestMountain() {
    mountain = new Mountain()
  }


  @Test
  void mountainIsInitializedProperly() {
    assertEquals 2, mountain.size()
    assertEquals 2, mountain[0].size()
    assertEquals 2, mountain[1].size()
  }


  @Test
  void currentScaleIsDerivedFromRowSize() {
    assertEquals 1, mountain.currentScale(new int[2])
    assertEquals 2, mountain.currentScale(new int[3])
    assertEquals 4, mountain.currentScale(new int[5])
    assertEquals 8, mountain.currentScale(new int[9])
    assertEquals 16, mountain.currentScale(new int[17])
    assertEquals 32, mountain.currentScale(new int[33])
  }


  @Test
  void nextScaleIsDerivedFromRowSize() {
    assertEquals 2, mountain.nextScale(new int[2])
    assertEquals 4, mountain.nextScale(new int[3])
    assertEquals 8, mountain.nextScale(new int[5])
    assertEquals 16, mountain.nextScale(new int[9])
    assertEquals 32, mountain.nextScale(new int[17])
    assertEquals 64, mountain.nextScale(new int[33])
  }


  @Test
  void randomNumbersScaleByRowSize() {
    mountain.scaleFunction = { scale -> (int)(64 / scale) }
    assertEquals 32, mountain.displacement(2)
    assertEquals 16, mountain.displacement(4)
    assertEquals 8, mountain.displacement(8)
    assertEquals 4, mountain.displacement(16)
    assertEquals 2, mountain.displacement(32)
    assertEquals 1, mountain.displacement(64)
  }


  @Test
  void midPointElevationAveragesAndAddsDisplacement() {
    def pair = [ 1, 2 ]
    assertEquals 1.75, mountain.midpointElevation(pair, { 0.25 }), 0.0000001
    assertEquals 1.25, mountain.midpointElevation(pair, { -0.25 })
  }


  @Test
  void tripleIsGeneratedFromPairWithDisplacement() {
    def pair = [ 1.0, 4.0 ]

    mountain.scaleFunction = { scale -> 1 / scale }
    def triple = mountain.createTripleFromPair(pair, mountain.nextScale(pair))
    assertEquals([1.0, 3.0, 4.0], triple)
  }


  @Test
  void displacingARowOfTwoCreatesThree() {
    def oldRow = [1.0, 2.0]
    mountain.scaleFunction = { scale -> scale }
    def newRow = mountain.displaceRow(oldRow)

    assertEquals 3, newRow.size()
    assertEquals([1.0, 3.5, 2.0], newRow)
  }


  @Test
  void displacingARowOfThreeCreatesFive() {
    def oldRow = [1.0, 3.5, 2.0]
    mountain.scaleFunction = { scale -> scale }
    def newRow = mountain.displaceRow(oldRow)

    assertEquals 5, newRow.size()
    assertEquals([1.0, 6.25, 3.5, 6.75, 2.0], newRow)
  }


  @Test(expected=IllegalArgumentException.class)
  void createARowFailsIfRowsAreDifferentSizes() {
    mountain.createMiddleRow([ 1, 2, 3 ], [ 4, 5, 6, 7, 8 ])
  }


  @Test
  void testCreatingARowForInsertion() {
    def topRow = [ 1, 2, 3 ]
    def bottomRow = [ 4, 5, 6 ]
    mountain.scaleFunction = { scale -> 0.5 }
    def middleRow = mountain.createMiddleRow(topRow, bottomRow)
    
    assertEquals 3, middleRow.size()
    assertEquals([3.0, 4.0, 5.0], middleRow)
  }


  @Test
  void growingADefaultMountain() {
    mountain.rows = [ [1.0, 1.0], [2.0, 2.0] ]
    mountain.scaleFunction = { scale -> 1.0 / scale }
    assertEquals 2, mountain.size()
    assertEquals 2, mountain[0].size()
    assertEquals 2, mountain[1].size()
    
    mountain.grow()
    assertEquals 3, mountain.size()
    assertEquals 3, mountain[0].size()
    assertEquals 3, mountain[1].size()
    assertEquals 3, mountain[2].size()
    assertEquals([1.0, 1.5, 1.0], mountain[0])
    assertEquals([2.0, 2.5, 2.0], mountain[1])
    assertEquals([2.0, 2.5, 2.0], mountain[2])

    mountain.grow()
    assertEquals 5, mountain.size()
    assertEquals 5, mountain[0].size()
    assertEquals 5, mountain[1].size()
    assertEquals 5, mountain[2].size()
    assertEquals 5, mountain[3].size()
    assertEquals 5, mountain[4].size()
    assertEquals([1.0,  1.50, 1.5,  1.50, 1.0], mountain[0])
    assertEquals([1.75, 2.25, 2.25, 2.25, 1.75], mountain[1])
    assertEquals([2.0,  2.50, 2.5,  2.50, 2.0], mountain[2])
    assertEquals([2.25, 2.75, 2.75, 2.75, 2.25], mountain[3])
    assertEquals([2.0,  2.50, 2.5,  2.50, 2.0], mountain[4])

    mountain.grow()
    assertEquals 9, mountain.size()
    assertEquals 9, mountain[0].size()
    assertEquals 9, mountain[1].size()
    assertEquals 9, mountain[2].size()
    assertEquals 9, mountain[3].size()
    assertEquals 9, mountain[4].size()
    assertEquals 9, mountain[5].size()
    assertEquals 9, mountain[6].size()
    assertEquals 9, mountain[7].size()
    assertEquals 9, mountain[8].size()
  }

}