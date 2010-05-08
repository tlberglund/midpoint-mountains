
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
    
    def middle = middlePoint.east
    assertNotNull middle
    assertEquals topMiddle, middle.north
    assertEquals bottomMiddle, middle.south
  }
  
  @Test
  void northSouthRowInsertion() {
    def northRow = northWest
    def southRow = southWest
    
    assertNotNull northRow
    assertNotNull southRow
    assertEquals northRow, southRow.north
    assertEquals southRow, northRow.south
    
    mountain.doInsertionsOnEastWestRow(northWest, 2, { 1 })
    mountain.doInsertionsOnEastWestRow(northWest.south, 2, { 1 })
    mountain.insertNewNorthSouthRow(northRow, 2, { 1 })
    
    println "TOP"
    northWest.eachEast { println it }
    println "MIDDLE"
    northWest.south.eachEast { println it }
    println "BOTTOM"
    northWest.south.south.eachEast { println it }
    
    def middleRow = northRow.south
    assertNotNull middleRow
    assertEquals middleRow, northRow.south
    assertEquals middleRow, southRow.north
    assertEquals northRow, middleRow.north
    assertEquals southRow, middleRow.south
    
    def northEast = northRow.east.east
    def southEast = southRow.east.east
    def middleEast = middleRow.east?.east
    
    assertNotNull northEast
    assertNotNull southEast
    assertNotNull middleRow
    assertNotNull middleEast
    assertEquals middleEast, northEast.south
    assertEquals middleEast, southEast.north
    assertEquals middleRow, northWest.south
    assertEquals middleRow, southWest.north
  }
  
  
}