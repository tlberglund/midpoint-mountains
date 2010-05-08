
package com.augusttechgroup.mountain

import org.junit.Test
import org.junit.Before
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
    def range = mountain.buildInitialRange(null, 1)
    
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
    
    westernPoint = mountain.doInsertionsOnEastWestRow(northWest, 2, { 1 })
    assertNotNull westernPoint
    assertNotNull westernPoint.east
    assertNotNull westernPoint.east.east
    assertEquals 2, westernPoint.east.scale
    assertEquals westernPoint, westernPoint.east.west
    assertEquals westernPoint.east, westernPoint.east.east.west
  }
  
  @Test
  void northSouthRowInsertion() {
    def northRow = northWest
    def southRow = southWest
    
    assertNotNull northRow
    assertNotNull southRow
    assertEquals northRow, southRow.north
    assertEquals southRow, northRow.south
    
    mountain.insertNewNorthSouthRow(northRow, 2, { 1 })
    
    assertFalse northRow.south == southRow
    assertFalse southRow.north == northRow
    assertEquals southRow, northRow.south.south
    assertEquals northRow, southRow.north.north
    
    def northEast = northRow.east
    def southEast = southRow.east
    
    assertNotNull northEast
    assertNotNull southEast
    assertFalse northEast.south == southEast
    assertFalse southEast.north == northEast
    
  }
  
  
}