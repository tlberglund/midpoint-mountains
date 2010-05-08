
package com.augusttechgroup.mountain

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*


class MountainPointInsertionTests {

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
  void testEastWestInsertion() {
    def nodes = mountain.insertBetweenEastAndWest(northWest, {})
    
    def west = nodes[0]
    def middle = nodes[1]
    def east = nodes[2]
    
    println west
    println middle
    println east
    
    assertNotNull west
    assertNotNull middle
    assertNotNull east

    assertEquals middle, west.east
    assertEquals middle, east.west
    assertEquals west, middle.west
    assertEquals east, middle.east
  }
  
}