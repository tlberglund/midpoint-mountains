
package com.augusttechgroup.mountain

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*


class PointTests {
  
  def northWest
  def northEast
  def southEast
  def southWest
  
  
  @Before
  void setUpPoints() {
    def displacer = { 1 }
    def scale = 1
    
    northWest = new Point(displace: displacer, scale: scale)
    northEast = new Point(displace: displacer, scale: scale)
    southEast = new Point(displace: displacer, scale: scale)
    southWest = new Point(displace: displacer, scale: scale)
    
    northWest.east = northEast
    northWest.south = southWest
    
    northEast.west = northWest
    northEast.south = southEast
    
    southEast.north = northEast
    southEast.west = southWest
    
    southWest.north = northWest
    southWest.east = southEast
  }
  
  
  @Test
  void singleEastInsertion() {    
    def middle = northWest.insertEast(2, { 2 })
    def west = middle.west
    def east = middle.east
    
    assertNotNull west
    assertNotNull middle
    assertNotNull east

    assertEquals 2, middle.scale
    assertEquals middle, west.east
    assertEquals middle, east.west
  }
  
  @Test
  void singleSouthInsertion() {
    def middle = northWest.insertSouth(2, { 2 })
    def north = middle.north
    def south = middle.south
    
    assertNotNull north
    assertNotNull middle
    assertNotNull south
    
    assertEquals 2, middle.scale
    assertEquals middle, north.south
    assertEquals middle, south.north
  }
  
}
