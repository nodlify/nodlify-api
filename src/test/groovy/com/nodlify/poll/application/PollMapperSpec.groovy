package com.nodlify.poll.application

import com.nodlify.poll.domain.Participant
import com.nodlify.poll.domain.Poll
import com.nodlify.poll.domain.TimeOption
import com.nodlify.test.extension.JsonMapper
import spock.lang.Specification
import spock.util.mop.Use

import static com.nodlify.poll.application.PollMapper.toPollData
import static com.nodlify.test.fixture.PollFixture.*

@Use(JsonMapper)
class PollMapperSpec extends Specification {

    def "should map to poll data"() {
        given:
        def poll = new Poll()
        poll.addOption(TimeOption.of(RANGE_08_01, WHOLE_DAY))
        poll.addOption(TimeOption.of(RANGE_08_02, WHOLE_DAY))
        poll.addOption(TimeOption.of(RANGE_08_03, WHOLE_DAY))
        poll.addOption(TimeOption.of(RANGE_08_04_14_15, NOT_WHOLE_DAY))
        poll.addOption(TimeOption.of(RANGE_08_04_15_16, NOT_WHOLE_DAY))
        poll.addOption(TimeOption.of(RANGE_08_04_16_17, NOT_WHOLE_DAY))
        poll.addParticipant(new Participant(DISPLAY_NAME))

        when:
        def data = toPollData(poll)

        then:
        data.options().size() == 6
        println(data.toJson())
    }
}
