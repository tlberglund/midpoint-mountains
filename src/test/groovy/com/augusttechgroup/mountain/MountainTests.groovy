
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


  @Test(expected=IllegalArgumentException.class)
  void tryingToInsertPastTheEndOfTheRowsFails() {
    mountain.insertRowAfterIndex(1)
  }


  @Test(expected=IllegalArgumentException.class)
  void tryingToInsertPastTheEndOfTheRowsFailsOnLargerMountains() {
    mountain.rows = [
      [ 1, 1, 1, 1, 1, 1, 1, 1 ],
      [ 2, 2, 2, 2, 2, 2, 2, 2 ],
      [ 3, 3, 3, 3, 3, 3, 3, 3 ],
      [ 4, 4, 4, 4, 4, 4, 4, 4 ],
      [ 5, 5, 5, 5, 5, 5, 5, 5 ]
    ]
    mountain.insertRowAfterIndex(4)
  }


  @Test
  void testInsertingARowInAMinimalMountain() {
    mountain.rows = [
      [ 1.0, 1.0, 1.0 ],
      [ 2.0, 2.0, 2.0 ]
    ]
    
    assertEquals 2, mountain.size()
    
    mountain.scaleFunction = { scale -> 0.1 }
    println mountain
    def newRow = mountain.insertRowAfterIndex(0)
    println mountain
    assertEquals 3, mountain.size()
    assertEquals 3, mountain[0].size()
    assertEquals 3, mountain[1].size()
    assertEquals 3, mountain[2].size()
    assertEquals([1.0, 1.0, 1.0], mountain[0])
    assertEquals([1.6, 1.6, 1.6], mountain[1])
    assertEquals([2.0, 2.0, 2.0], mountain[2])
    assertEquals newRow, mountain[1]
  }


  @Test
  void testInsertingTheSecondRow() {
    mountain.rows = [
      [ 1.0, 1.0, 1.0, 1.0, 1.0],
      [ 2.0, 2.0, 2.0, 2.0, 2.0],
      [ 3.0, 3.0, 3.0, 3.0, 3.0]
    ]
    
    mountain.scaleFunction = { scale -> 0.1 }
    def newRow = mountain.insertRowAfterIndex(1)
    assertEquals 4, mountain.size()
    assertEquals 5, mountain[1].size()
    assertEquals 5, mountain[2].size()
    assertEquals 5, mountain[3].size()
    assertEquals([2.0, 2.0, 2.0, 2.0, 2.0], mountain[1])
    assertEquals([2.6, 2.6, 2.6, 2.6, 2.6], mountain[2])
    assertEquals([3.0, 3.0, 3.0, 3.0, 3.0], mountain[3])
    assertEquals newRow, mountain[2]
  }


  @Test
  void testInsertingARowInTheMiddleOfAMountain() {
    mountain.rows = [
      [ 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 ],
      [ 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0 ],
      [ 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0 ],
      [ 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0 ],
      [ 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0 ]
    ]
    
    mountain.scaleFunction = { scale -> -0.1 }
    def newRow = mountain.insertRowAfterIndex(3)
    assertEquals 6, mountain.size()
    assertEquals 9, mountain[4].size()
    assertEquals([4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0], mountain[3])
    assertEquals([4.4, 4.4, 4.4, 4.4, 4.4, 4.4, 4.4, 4.4, 4.4], mountain[4])
    assertEquals([5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0], mountain[5])
    assertEquals 9, newRow.size()
    assertEquals newRow, mountain[4]
  }



  @Ignore
  @Test
  void testInterpolation() {
    def firstMountain = createThreeByThreeMountainWith([[1,2,3],[4,5,6],[7,8,9]])
    def secondMountain = createThreeByThreeMountainWith([[2,4,6],[8,10,12],[14,16,18]])
    def elevation = secondMountain.interpolate(firstMountain)
    
    assertNotNull elevation
    
    assertEquals 1.5,  elevation[0][0], 0.00000001
    assertEquals 3,    elevation[0][1], 0.00000001
    assertEquals 4.5,  elevation[0][2], 0.00000001
    assertEquals 6,    elevation[1][0], 0.00000001
    assertEquals 7.5,  elevation[1][1], 0.00000001
    assertEquals 9,    elevation[1][2], 0.00000001
    assertEquals 10.5, elevation[2][0], 0.00000001
    assertEquals 12,   elevation[2][1], 0.00000001
    assertEquals 13.5, elevation[2][2], 0.00000001
  }
  
  
  @Ignore
  @Test
  void testDelta() {
    def firstMountain = createThreeByThreeMountainWith([[1,2,3],[4,5,6],[7,8,9]])
    def secondMountain = createThreeByThreeMountainWith([[2,4,6],[8,10,12],[14,16,18]])
    def delta = firstMountain.delta(secondMountain.toArray())
    
    assertNotNull delta
    
    assertEquals 1, delta[0][0], 0.00000001
    assertEquals 2, delta[0][1], 0.00000001
    assertEquals 3, delta[0][2], 0.00000001
    assertEquals 4, delta[1][0], 0.00000001
    assertEquals 5, delta[1][1], 0.00000001
    assertEquals 6, delta[1][2], 0.00000001
    assertEquals 7, delta[2][0], 0.00000001
    assertEquals 8, delta[2][1], 0.00000001
    assertEquals 9, delta[2][2], 0.00000001
  }
  
  
  def createThreeByThreeMountainWith(elevations) {
    def m = new Mountain()
    m.initialize()
    def northRow = m.northWest
    def southRow = northRow.south
    def middleRow
    
    m.doInsertionsOnEastWestRow(northRow, 2, { 1 })
    m.doInsertionsOnEastWestRow(southRow, 2, { 1 })
    middleRow = m.insertBetweenRows(northRow, southRow, 2, { 1 })
    
    northRow.elevation = elevations[0][0]
    northRow.east.elevation = elevations[0][1]
    northRow.east.east.elevation = elevations[0][2]
    middleRow.elevation = elevations[1][0]
    middleRow.east.elevation = elevations[1][1]
    middleRow.east.east.elevation = elevations[1][2]
    southRow.elevation = elevations[2][0]
    southRow.east.elevation = elevations[2][1]
    southRow.east.east.elevation = elevations[2][2]
    
    return m
  }


}