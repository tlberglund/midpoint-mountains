
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
    def range = mountain.buildInitialRange({ 1 }, 1)
    
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
  
  
}