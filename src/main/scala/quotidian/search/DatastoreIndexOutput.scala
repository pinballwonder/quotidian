package quotidian.search

import org.apache.lucene.store.IndexOutput

class DatastoreIndexOutput(private var file:DatastoreFile) extends IndexOutput {
	def this() = this(DatastoreFile())
	def close:Unit = { /* nothing to do here */ }
	def flush:Unit = DatastoreDirectory.save(file)
	def getFilePointer():Long = file.position
	def length:Long = file.length
	def seek(pos:Long):Unit = file = file.seek(pos)
	def writeByte(b:Byte):Unit = file = file.write(b)
	def writeBytes(bytes:Array[Byte],offset:Int,length:Int):Unit = { }
}