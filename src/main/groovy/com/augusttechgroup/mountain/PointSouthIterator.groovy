//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

package com.augusttechgroup.mountain


class PointSouthIterator
  implements Iterator<Point> {
  
  def point
  
  PointSouthIterator(point) {
    this.point = point
  }
  
  boolean hasNext() {
    point != null
  }
  
  Point next() {
    if(point) {
      def next = point
      point = point.south
      return next
    }
    else {
      throw new NoSuchElementException()
    }
  }
  
  void remove() {
    throw new UnsupportedOperationException("Can't remove Points using the Point iterator")
  }
}
