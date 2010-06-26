
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
    def pair = [ 1, 4 ]

    mountain.scaleFunction = { scale -> 1 / scale }
    def triple = mountain.insertNewPoint(pair, mountain.nextScale(pair))
    assertEquals 1, triple[0], 0.0000001
    assertEquals 3, triple[1], 0.0000001
    assertEquals 4, triple[2], 0.0000001
  }


  @Test
  void displacingARowOfTwoCreatesThree() {
    def oldRow = [1, 2]
    mountain.scaleFunction = { scale -> scale }
    def newRow = mountain.displaceRow(oldRow)

    assertEquals 3, newRow.size()
    assertEquals 1, newRow[0], 0.0000001
    assertEquals 3.5, newRow[1], 0.0000001
    assertEquals 2, newRow[2], 0.0000001
  }

  @Ignore
  @Test
  void eastWestRowInsertion() {
    def westernPoint = northWest
    assertNotNull westernPoint
    assertNotNull westernPoint.east
    assertNull westernPoint.east.east
    
    mountain.doInsertionsOnEastWestRow(northWest, 2, { 1 })
    def middlePoint = westernPoint.east
    assertNotNull middlePoint
    def easternPoint = middlePoint.east
    assertNotNull easternPoint

    assertEquals 2, middlePoint.scale
    assertEquals westernPoint, middlePoint.west
    assertEquals middlePoint, easternPoint.west
    
    assertEquals southWest, northWest.south
    assertEquals southEast, northEast.south

    def bottomMiddle = southWest.east
    assertNotNull bottomMiddle
    
    def topMiddle = northWest.east
    assertNotNull topMiddle
  }
  
  @Ignore
  @Test
  void northSouthRowInsertion() {
    def northRow = northWest
    def southRow = southWest
    
    assertNotNull northRow
    assertNotNull southRow
    assertEquals northRow, southRow.north
    assertEquals southRow, northRow.south
    
    mountain.doInsertionsOnEastWestRow(northRow, 2, { 1 })
    mountain.doInsertionsOnEastWestRow(southRow, 2, { 1 })
    def middleRow = mountain.insertBetweenRows(northRow, southRow, 2, { 1 })
        
    assertNotNull middleRow
    
    def nw = northRow
    def nm = northRow.east
    def ne = northRow.east?.east
    def mw = middleRow
    def mm = middleRow.east
    def me = middleRow.east?.east
    def sw = southRow
    def sm = southRow.east
    def se = southRow.east?.east
    
    assertNotNull nw
    assertNotNull nm
    assertNotNull ne
    assertNotNull mw
    assertNotNull mm
    assertNotNull me
    assertNotNull sw
    assertNotNull sm
    assertNotNull se
    assertEquals mw, nw.south
    assertEquals mw, sw.north
    assertEquals mm, nm.south
    assertEquals mm, sm.north
    assertEquals me, ne.south
    assertEquals me, se.north
    assertEquals nw, mw.north
    assertEquals sw, mw.south
    assertEquals nm, mm.north
    assertEquals sm, mm.south
    assertEquals ne, me.north
    assertEquals se, me.south
  }


  @Ignore
  @Test
  void arrayConversion() {
    northWest.elevation = 1
    northEast.elevation = 2
    southWest.elevation = 3
    southEast.elevation = 4
    def array = mountain.toArray()
    
    assertEquals 2, array.size()
    def northRow = array[0]
    def southRow = array[1]
    
    assertNotNull northRow
    assertNotNull southRow
    assertEquals 2, northRow.size()
    assertEquals 2, southRow.size()
    
    assertEquals 1, northRow[0], 0.00000001
    assertEquals 2, northRow[1], 0.00000001
    assertEquals 3, southRow[0], 0.00000001
    assertEquals 4, southRow[1], 0.00000001
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