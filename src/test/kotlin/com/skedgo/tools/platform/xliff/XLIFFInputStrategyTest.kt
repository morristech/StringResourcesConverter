package com.skedgo.tools.platform.xliff

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test
import rx.Observable

class XLIFFInputStrategyTest {

    @Test
    fun `should create a strings structure`() {
        // Arrange.
        val xliffStream = this.javaClass.classLoader
                .getResourceAsStream("es.xliff")

        // Act & Assert.
        val translation = XLIFFInputStrategy().createInputValues(xliffStream)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertCompleted()
                .onNextEvents[0]

        // Assert.
        translation.transUnits.size `should be` 3
        translation.transUnits[0].id `should equal` "test"
        translation.transUnits[0].source `should equal` "Some text"
        translation.transUnits[0].target `should equal` "Algun texto"
        translation.transUnits[0].note `should equal` "Some text example"
        translation.transUnits[1].id `should equal` "Agenda"
        translation.transUnits[1].source `should equal` "Agenda"
        translation.transUnits[1].target `should equal` "Agenda"
        translation.transUnits[1].note `should equal` "Title for button to access agenda"
        translation.transUnits[2].id `should equal` "Alerts"
        translation.transUnits[2].source `should equal` "Alerts"
        translation.transUnits[2].target `should equal` "Alertas"
        translation.transUnits[2].note `should equal` "No comments..."
    }

    @Test
    fun `should create a strings structure with no comments`() {
        // Arrange.
        val xliffStream = this.javaClass.classLoader
                .getResourceAsStream("es_no_comments.xliff")

        // Act & Assert.
        val translation = XLIFFInputStrategy().createInputValues(xliffStream)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertCompleted()
                .onNextEvents[0]

        // Assert.
        translation.transUnits.size `should be` 3
        translation.transUnits[0].id `should equal` "test"
        translation.transUnits[0].source `should equal` "Some text"
        translation.transUnits[0].target `should equal` "Algun texto"
        translation.transUnits[0].note `should equal` ""
        translation.transUnits[1].id `should equal` "Agenda"
        translation.transUnits[1].source `should equal` "Agenda"
        translation.transUnits[1].target `should equal` "Agenda"
        translation.transUnits[1].note `should equal` ""
        translation.transUnits[2].id `should equal` "Alerts"
        translation.transUnits[2].source `should equal` "Alerts"
        translation.transUnits[2].target `should equal` "Alerts"
        translation.transUnits[2].note `should equal` ""
    }

    @Test
    fun `should create a strings structure from ios like strings`() {
        // Arrange.
        val xliffStream = this.javaClass.classLoader
                .getResourceAsStream("es_ios.xliff")

        // Act & Assert.
        val translation = XLIFFInputStrategy().createInputValues(xliffStream)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertCompleted()
                .onNextEvents[0]

        // Assert.
        translation.transUnits.size `should be` 1
        translation.transUnits[0].target `should equal` "1. Seleccione su viaje\n" +
                "2. Eleja ticket y configure el método de pago\n" +
                "3. Escanéa el código QR para utilizar el ticket\n" +
                "4. Sus tickets se encuentran en Mis reservas"
    }

    @Test
    fun `should create a strings structure from multiple sources`() {
        // Arrange.
        val xliffStreamES = this.javaClass.classLoader
                .getResourceAsStream("es.xliff")
        val xliffStreamEN = this.javaClass.classLoader
                .getResourceAsStream("en.xliff")

        // Act & Assert.
        val translation =
                Observable.from(listOf(xliffStreamES, xliffStreamEN))
                        .flatMapSingle { XLIFFInputStrategy().createInputValues(it) }
                        .test()
                        .assertNoErrors()
                        .assertValueCount(2)
                        .assertCompleted()
                        .onNextEvents

        // Assert.
        translation[0].transUnits.size `should be` 3
        translation[1].transUnits.size `should be` 3
    }
}