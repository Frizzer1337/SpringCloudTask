package kafka.practice.api.entity

import com.frizzer.kafkaapi.entity.Credit
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class CollectorCredit(val credit: Credit) {
    val id = ObjectId().toString()
    val collectorId: String = "-1"
    val creditId: String = credit.id
    val lastCallDate: String = LocalDateTime.now().toString()
}