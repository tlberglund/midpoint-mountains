//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

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
    
    northWest = new Point(displacer: displacer, scale: scale)
    northEast = new Point(displacer: displacer, scale: scale)
    southEast = new Point(displacer: displacer, scale: scale)
    southWest = new Point(displacer: displacer, scale: scale)
    
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
  
  @Test
  void testEastIterator() {
    def p3 = new Point()
    def p2 = new Point(east: p3)
    def p1 = new Point(east: p2)
    def points = [p1, p2, p3]
    def iteratedPoints = []
    def index = 0
    
    println p1
    println p2
    println p3
    
    def iter = p1.eastIterator()
    while(iter.hasNext()) {
      def point = iter.next()
      assertNotNull point
      iteratedPoints << point
    }
    
    assertEquals points.size(), iteratedPoints.size()
    assertEquals points[0], iteratedPoints[0]
    assertEquals points[1], iteratedPoints[1]
    assertEquals points[2], iteratedPoints[2]
  }
  
  @Test
  void testSouthIterator() {
    def p3 = new Point()
    def p2 = new Point(south: p3)
    def p1 = new Point(south: p2)
    def points = [p1, p2, p3]
    def iteratedPoints = []
    def index = 0
    
    def iter = p1.southIterator()
    while(iter.hasNext()) {
      def point = iter.next()
      assertNotNull point
      iteratedPoints << point
    }
    
    assertEquals points.size(), iteratedPoints.size()
    assertEquals points[0], iteratedPoints[0]
    assertEquals points[1], iteratedPoints[1]
    assertEquals points[2], iteratedPoints[2]
  }
  
  @Test
  void testEachEast() {
    def p3 = new Point()
    def p2 = new Point(east: p3)
    def p1 = new Point(east: p2)
    def points = [p1, p2, p3]
    
    def count = 0
    p1.eachEast { point ->
      assertEquals point, points[count]
      count++ 
    }
    
    assertEquals 3, count
  }

  @Test
  void testEachSouth() {
    def p3 = new Point()
    def p2 = new Point(south: p3)
    def p1 = new Point(south: p2)
    def points = [p1, p2, p3]
    
    def count = 0
    p1.eachSouth { point -> 
      assertEquals point, points[count]
      count++ 
    }
    
    assertEquals 3, count
  }
  
  @Test
  void testCollectEast() {
    def p3 = new Point()
    def p2 = new Point(east: p3)
    def p1 = new Point(east: p2)
    
    def list = p1.collectEast { point -> point }
    assertNotNull list
    assertEquals 3, list.size()
    assertEquals p1, list[0]
    assertEquals p2, list[1]
    assertEquals p3, list[2]
  }
  
  @Test
  void testCollectSouth() {
    def p3 = new Point()
    def p2 = new Point(south: p3)
    def p1 = new Point(south: p2)
    
    def list = p1.collectSouth { point -> point }
    assertNotNull list
    assertEquals 3, list.size()
    assertEquals p1, list[0]
    assertEquals p2, list[1]
    assertEquals p3, list[2]
  }
  
  
  @Test
  void testDisplace() {
    def point = new Point(displacer: {1})
    assertEquals 0, point.elevation, 0.00000001
    point.displace()
    assertEquals 1, point.elevation, 0.00000001
    point.displace(0.5)
    assertEquals 1.5, point.elevation, 0.00000001

    point = new Point(displacer: { -0.32 })
    assertEquals 0, point.elevation, 0.00000001
    point.displace()
    assertEquals(-0.32, point.elevation, 0.00000001)
    
    point = new Point(elevation: 100, displacer: { point.elevation / 2 })
    assertEquals 100, point.elevation, 0.00000001
    point.displace()
    assertEquals 50, point.elevation, 0.00000001
  }
  
}
