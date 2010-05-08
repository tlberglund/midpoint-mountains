
package com.augusttechgroup.mountain


/**
 X X X X X
 X X X X X
 X X X X X
 X X X X X
 X X X X X
 
        north
 west         east
       south
 
 elevation

 **/
class Mountain {

  Random random
  Node northWest
  Closure scaleFunction
  
  Mountain() {
    random = new Random()
    random.setSeed(new Date().time)
    
    def rootDisplacer = this.&gaussian.curry(1)
    
    northWest = new Node(displace: rootDisplacer)
    def northEast = new Node(displace: rootDisplacer)
    def southEast = new Node(displace: rootDisplacer)
    def southWest = new Node(displace: rootDisplacer)
    
    northWest.east = northEast
    northWest.south = southWest
    
    northEast.west = northWest
    northEast.south = southEast
    
    southEast.north = northEast
    southEast.west = southWest
    
    southWest.north = northWest
    southWest.easy = southEast
  }
  
  
  def addMidpoints() {
    def node = northWest

  }
  
  def insertBetweenWestAndEast(node, displacer) {
    if(node && node.east) {
      def newNode = new Node()
      newNode.displacer = displacer
      newNode.north = node.north
      newNode.south = node.south
      newNode.west = node
      newNode.east = node.east
      node.east = node
      node.east.west = node
    }
  }
  
  
  def insertBetweenNorthAndSouth(node, displacer) {
    if(node && node.south) {
      def newNode = new Node()
      newNode.displacer = displacer
      newNode.north = node.north
      newNode.south = node.south
      newNode.west = node
      newNode.east = node.east
      node.east = node
      node.east.west = node
    }
  }
  
  
  
  
  double gaussian(double scale) {
    random.nextGaussian() / scale
  }
      
}

