package kafka.practice.api.entity

import com.frizzer.kafkaapi.entity.CollectorActivityStatus

class Collector (
    var id: String,
    var firstName : String,
    var lastName : String,
    var collectorActivityStatus: CollectorActivityStatus,
)