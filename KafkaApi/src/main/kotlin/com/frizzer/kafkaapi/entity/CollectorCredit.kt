package kafka.practice.api.entity

import com.frizzer.kafkaapi.entity.Credit
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class CollectorCredit(val credit : Credit){
    var id = ObjectId().toString()
    var collectorId: String? = "-1"
    var creditId: String? = credit.id
    var lastCallDate: String? = LocalDateTime.now().toString()
}