//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

package com.augusttechgroup.mountain

def scale = 7

m1 = new Mountain()
scale.times { m1.grow() }

m2 = new Mountain()
scale.times { m2.grow() }

m3 = new Mountain()
scale.times { m3.grow() }

m4 = new Mountain()
scale.times { m4.grow() }

m12Tweens = m1.tweens(m2, 20)
m23Tweens = m2.tweens(m3, 20)
m34Tweens = m3.tweens(m4, 20)
m41Tweens = m4.tweens(m1, 20)

def count = 1
def df = new java.text.DecimalFormat('00')
[m12Tweens, m23Tweens, m34Tweens, m41Tweens].each { tweens ->
  tweens.each { tween ->
    def file = new File("data/mountain-${df.format(count)}.txt")
    file.withPrintWriter { pw ->
      tween.each { row ->
        pw.println row.join('\t')
      }
    }
    count++
  }
}