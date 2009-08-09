package quotidian.search

import com.google.appengine.api.datastore.Blob
import quotidian.DatastoreSpecification

object DatastoreFileSpecs extends DatastoreSpecification {
	"An empty file" should {
		"have a position at the start" >> {
			val file = DatastoreFile()
			file.position mustEqual 0
		}
		"have a single zero byte" >> {
			val file = DatastoreFile()
			val bf = file.read
			bf.byte mustEqual 0
			file.length mustEqual 1
		}
		"be able to read beyond it's current capacity without error" >> {
			val file = DatastoreFile()
			val bf = file.read.file.read
			bf.file.length mustEqual 3
		}
		"be able to write bytes" >> {
			val file = DatastoreFile()
			file.position mustEqual 0
			val bits = new Array[Byte](1)
			file.write(15).read(bits,0,1)
			bits(0) mustEqual 15
		}
	}
	"A file with bytes written" should {
		"provide an entity with the written bytes contained" >> {
			val ints = (0 until 15)
			val bytes = for (i <- ints) yield i.asInstanceOf[Byte]
			val file = DatastoreFile()
			val bits = new Array[Byte](15)
			file.write(bytes.toArray,0,15).read(bits,0,15)
			bits must haveTheSameElementsAs(bytes)
		}
	}
}
