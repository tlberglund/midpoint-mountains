
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
    
    def middle = middlePoint.east
    assertNotNull middle
    assertEquals topMiddle, middle.north
    assertEquals bottomMiddle, middle.south
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
  
  @Test void testNorthSouthLinkMaintenance() {
    def nw = new Point()
    def nm = new Point()
    def ne = new Point()
    def mw = new Point()
    def mm = new Point()
    def me = new Point()
    def sw = new Point()
    def sm = new Point()
    def se = new Point()
    nw.east = nm
    nm.west = nw
    nm.east = ne
    ne.west = nm
    nw.south = mw
    mw.east = mm
    mm.west = mw
    mm.east = me
    me.west = mm
    mw.north = nw
    mw.south = sw
    sw.east = sm
    sm.west = sw
    sm.east = se
    se.west = sm
    sw.north = mw
    
    println "nw=${nw}"
    println "nm=${nm}"
    println "ne=${ne}"
    println "mw=${mw}"
    println "mm=${mm}"
    println "me=${me}"
    println "sw=${sw}"
    println "sm=${sm}"
    println "se=${se}"
    
    mountain.maintainNorthSouthLinks(nw)
    mountain.maintainNorthSouthLinks(mw)
    mountain.maintainNorthSouthLinks(sw)
    assertEquals sw, mw.south
    assertEquals nm, nw.east
    assertEquals nw, nm.west
    assertEquals ne, nm.east
    assertEquals nm, ne.west
    assertEquals mm, mw.east
    assertEquals mw, mm.west
    assertEquals me, mm.east
    assertEquals mm, me.west
    assertEquals sm, sw.east
    assertEquals sw, sm.west
    assertEquals se, sm.east
    assertEquals sm, se.west
    assertEquals mw, nw.south
    assertEquals mm, nm.south
    assertEquals me, ne.south
    assertEquals sw, mw.south
    assertEquals sm, mm.south
    assertEquals se, me.south
    assertNull sw.south
    assertNull sm.south
    assertNull se.south
    assertEquals mw, sw.north
    assertEquals mm, sm.north
    assertEquals me, se.north
    assertEquals nw, mw.north
    assertEquals nm, mm.north
    assertEquals ne, me.north
    assertNull nw.north
    assertNull nm.north
    assertNull ne.north
  }
  
  
}