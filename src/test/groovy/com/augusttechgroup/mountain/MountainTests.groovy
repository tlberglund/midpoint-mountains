
package com.augusttechgroup.mountain

import org.junit.Test
import org.junit.Before
import org.junit.Ignore
import static org.junit.Assert.*


class MountainTests {

  def mountain
  Point northWest
  Point northEast
  Point southEast
  Point southWest


  @Before
  void buildTestMountain() {
    mountain = new Mountain()
    def range = mountain.initialize()
    
    (northWest, northEast) = range[0]
    (southWest, southEast) = range[1]
  }


  @Test
  void testInitialMountain() {
    assertEquals 1, northWest.scale
    assertEquals 1, northEast.scale
    assertEquals 1, southEast.scale
    assertEquals 1, southWest.scale
    assertEquals null, northWest.west
    assertEquals null, northWest.north
    assertEquals northEast, northWest.east
    assertEquals southWest, northWest.south
    assertEquals null, southWest.west
    assertEquals null, southWest.south
    assertEquals northWest, southWest.north
    assertEquals southEast, southWest.east
    assertEquals null, northEast.north
    assertEquals null, northEast.east
    assertEquals northWest, northEast.west
    assertEquals southEast, northEast.south
    assertEquals null, southEast.east
    assertEquals null, southEast.south
    assertEquals northEast, southEast.north
    assertEquals southWest, southEast.west
  }

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