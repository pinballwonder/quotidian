package quotidian.web.controller

import org.apache.lucene.index.IndexReader
import quotidian.DatastoreSpecification
import quotidian.model.Quote

object DatastoreQuoteControllerSpecs extends DatastoreSpecification {
	val controller = new DatastoreQuoteController
	"A controller" should {
		datastoreCleanup.before
		val quote = Quote("text","source","context")
		"save a quote" >> {
			val key = controller.save(quote)
			key must notBeNull
		}
		"have a searcher with a reader with terms" >> {
			controller.save(quote)
			val terms = controller.searcher.getIndexReader.terms
			terms.next mustEqual true
		}
	}
}
