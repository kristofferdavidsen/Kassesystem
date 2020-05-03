package com.pos.kasse.views

import com.pos.kasse.entities.Bruker
import com.pos.kasse.services.LoginService
import com.pos.kasse.styles.Footer
import com.pos.kasse.styles.LoginStyle
import com.pos.kasse.styles.Navbar
import com.pos.kasse.utils.Logger
import javafx.beans.binding.StringBinding
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventDispatchChain
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.paint.Color
import tornadofx.*
import java.util.function.Predicate.not
import kotlin.properties.Delegates


class Login : View() {

    private val topView: Top by inject()
    private val bottomView: Bottom by inject()
    private val centerView: Center by inject()

    init {
        title = "Login"
    }

    override val root = borderpane {
        top = topView.root
        center = centerView.root
        bottom = bottomView.root
    }

    class Top : View() {

        override val root = label("Davidsens Matkolonial") {
            addClass(Navbar.wrapper)
        }
    }

    class Center : View() {

        /*TODO: Fikse state mellom views ved bruk av ViewContainer.pack()/unpack().
                Fikse events relatert til feil brukernavn/passord.    */

        private var loginCode = 666
        private val loginService: LoginService by di()
        private val bruker = Bruker()
        private val brukernavnProp = SimpleStringProperty()
        private val passordProp = SimpleStringProperty()
        private var errorProp = SimpleIntegerProperty(loginCode)
        private var errorMsg = errorProp.stringBinding{
            "%+d".format(it)
        }


        override val root = form {
            addClass(LoginStyle.form)
            fieldset("Login") {
                label(errorMsg) //TODO: Lage Event for denne.
                field("Brukernavn") {
                    textfield().bind(brukernavnProp)
                }
                field("Passord") {
                    passwordfield().bind(passordProp)
                }
                button("GO...") {
                    setOnAction {
                        bruker.brukernavn = brukernavnProp.get()
                        bruker.passord = passordProp.get()
                        loginCode = performLogin(bruker)
                        if (loginCode == 0) {
                            //TODO: Bytter kun ut center av borderpane, må bytte hele vinduet.
                            replaceWith<MainWindow>()
                        }
                    }
                    //TODO: Midlertidig løsning på null-props.
                    disableProperty().bind(brukernavnProp.isNull.or(passordProp.isNull))
                }
            }

        }

        private fun performLogin(bruker: Bruker): Int {
            val logger = Logger()
            val loginMessage: String

            if (loginService.kontrollerLogin(bruker)) {
                loginCode = 0
                loginMessage = "Kode: ${loginCode}, ${bruker.brukernavn} logges inn..."
            } else {
                loginCode = 1
                loginMessage = "Kode: ${loginCode}, ${bruker.brukernavn} er ikke logget inn!"
            }
            logger.printConsole(loginMessage)
            return loginCode
        }
    }
    
    class Bottom : View() {
        override val root = label("Tester bunntekst") {
            addClass(Footer.wrapper)
        }

    }
}
