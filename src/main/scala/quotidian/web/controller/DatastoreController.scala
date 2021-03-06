package quotidian.web.controller

import com.google.appengine.api.datastore.{Key,KeyFactory}
import com.google.appengine.api.labs.taskqueue.{Queue,QueueFactory,TaskOptions}
import java.io.Serializable
import quotidian.Config
import quotidian.model.Quote
import quotidian.persistence.datastore.DatastorePersister
import com.bryanjswift.lucene.store.gae.DatastoreDirectory

trait DatastoreController {
	protected val persister = DatastoreController.persister
	protected val directory = DatastoreController.directory
	protected lazy val MaxPerPage = Config.MaxPerPage
}

private object DatastoreController {
	private val persister = DatastorePersister
	private val directory = new DatastoreDirectory
}

class DatastoreQuoteController extends QuoteController with DatastoreController {
	def enqueue(id:Serializable):Unit = {
		val handle = QueueFactory.getQueue(Config.IndexQuoteQueue).add(TaskOptions.Builder.url("/task/index-quote")
			.method(TaskOptions.Method.POST).param(Quote.Id,KeyFactory.keyToString(id.asInstanceOf[Key])))
		info(String.format("Indexing job queued in %s with id %s",handle.getQueueName,handle.getName))
	}
}
class DatastoreSearchController extends SearchController with DatastoreController
