import React, {Component} from "react";
import axios from 'axios'
import logo from './res/logo.png'

export default class Register extends Component {


    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            text: true
        }


    }

    createUser() {
        if (this.handleValidation()) {
            const params = new URLSearchParams();
            params.append('username', this.inputLogin.value);
            params.append('password', this.inputPassword.value);
            params.append('mail', this.inputEmail.value);
            params.append('nom', this.inputLastname.value);
            params.append('prenom', this.inputFirsname.value);
            params.append('date', this.inputDate.value);
            params.append('sexe', true);

            axios.post("http://localhost:8080/TwisterFinal/CreateUser", params)
                .then(res => {
                    if (res.data.code === undefined)
                        this.props.signin();
                    else {
                        let errors = {};
                        errors["serverError"] = res.data.message;
                        this.setState({
                            errors: errors
                        })
                    }

                });
        }
        console.log(this.state.errors)


    }


    handleValidation() {
        let errors = {};
        let formIsValid = true;

        //login
        if (this.inputLogin.value === "") {
            formIsValid = false;
            errors["login"] = "Login: Cannot be empty";
        }
        else if (this.inputLogin.value.length < 5) {
            this.inputLogin.value = "";
            formIsValid = false;
            errors["login"] = "Login: Too short, the minimum is 6 letters";
        }
        else if (!this.inputLogin.value.match(/^[a-zA-Z]+$/)) {
            this.inputLogin.value = "";
            formIsValid = false;
            errors["login"] = "Login: Only letters";
        }


        //firstname
        if (this.inputFirsname.value === "") {
            formIsValid = false;
            errors["firstname"] = "Prenom: Cannot be empty";
        }
        else {
            if (!this.inputFirsname.value.match(/^[a-zA-Z]+$/)) {
                this.inputFirsname.value = "";

                formIsValid = false;
                errors["firstname"] = "Prenom: Only letters";
            }
        }


        //password
        if (this.inputPassword.value === "") {
            formIsValid = false;
            errors["password"] = "Password: Cannot be empty";
        }
        else {
            if (this.inputPassword.value.length < 5) {
                this.inputPassword.value = "";

                formIsValid = false;
                errors["password"] = "Password: Too short, the minimum is 6 letters";
            }
        }

        //lastname
        if (this.inputLastname.value === "") {
            formIsValid = false;
            errors["lastname"] = "Nom: Cannot be empty";
        }
        else {
            if (!this.inputLastname.value.match(/^[a-zA-Z]+$/)) {
                this.inputLastname.value = "";

                formIsValid = false;
                errors["lastname"] = "Nom: Only letters";
            }
        }

        //date
        if (this.inputDate.value === "") {
            formIsValid = false;
            errors["date"] = "Date: Cannot be empty";
        }
        const reg = /^((((19|[2-9]\d)\d{2})\-(0[13578]|1[02])\-(0[1-9]|[12]\d|3[01]))|(((19|[2-9]\d)\d{2})\-(0[13456789]|1[012])\-(0[1-9]|[12]\d|30))|(((19|[2-9]\d)\d{2})\-02\-(0[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))\-02\-29))$/g;

        if (!this.inputDate.value.match(reg)) {
            this.inputDate.value = "";
            formIsValid = false;
            errors["date"] = "Date: Mauvais format";
        }

        //Email
        if (this.inputEmail.value === "") {
            formIsValid = false;
            errors["email"] = "Email: Cannot be empty";
        }
        else {
            let lastAtPos = this.inputEmail.value.lastIndexOf('@');
            let lastDotPos = this.inputEmail.value.lastIndexOf('.');

            if (!(lastAtPos < lastDotPos && lastAtPos > 0 && this.inputEmail.value.indexOf('@@') == -1 && lastDotPos > 2 && (this.inputEmail.value.length - lastDotPos) > 2)) {
                this.inputEmail.value = "";

                formIsValid = false;
                errors["email"] = "Email: Is not valid";
            }
        }

        this.setState({errors: errors});
        return formIsValid;
    };


    render() {

        return (
            <div className="input_container">
                <div className="logos">
                    <a>
                        <img src={logo} onClick={() => this.props.signin()} alt=""/>
                    </a>
                </div>
                <div className="register">
                    <span className="errors">{this.state.errors["serverError"]}</span>

                    <span className="errors">{this.state.errors["login"]}</span>

                    <input onKeyPress={
                        event => {
                            if (event.key === "Enter")
                                this.createUser();
                        }
                    }

                           ref={(inputLogin) => {
                               this.inputLogin = inputLogin
                           }}
                           type="text" name="login" placeholder="Login"/>

                    <span className="errors">{this.state.errors["password"]}</span>

                    <input
                        onKeyPress={
                            event => {
                                if (event.key === "Enter")
                                    this.createUser();
                            }
                        }
                        ref={(inputPassword) => {
                            this.inputPassword = inputPassword
                        }}
                        type="password" name="pass" placeholder="Mot de passe"/>

                    <span className="errors">{this.state.errors["email"]}</span>

                    <input
                        onKeyPress={
                            event => {
                                if (event.key === "Enter")
                                    this.createUser();
                            }
                        }
                        ref={(inputEmail) => {
                            this.inputEmail = inputEmail
                        }}
                        type="email" name="email" placeholder="Adresse e-mail"/>

                    <span className="errors">{this.state.errors["firstname"]}</span>

                    <input
                        onKeyPress={
                            event => {
                                if (event.key === "Enter")
                                    this.createUser();
                            }
                        }
                        ref={(inputFirsname) => {
                            this.inputFirsname = inputFirsname
                        }}
                        type="text" name="firstname" placeholder="Nom"/>

                    <span className="errors">{this.state.errors["lastname"]}</span>

                    <input
                        onKeyPress={
                            event => {
                                if (event.key === "Enter")
                                    this.createUser();
                            }
                        }
                        ref={(inputLastname) => {
                            this.inputLastname = inputLastname
                        }}
                        type="text" name="lastname" placeholder="Pr&eacute;noms"/>

                    <span className="errors">{this.state.errors["date"]}</span>

                    <input
                        onKeyPress={
                            event => {
                                if (event.key === "Enter")
                                    this.createUser();
                            }
                        }
                        ref={(inputDate) => {
                            this.inputDate = inputDate
                        }}
                        placeholder="Date de naissance format: AAAA-MM-JJ" type={this.state.text ? "text" : "date"}
                        onBlur={() => {
                            this.setState({text: true})
                        }}
                        onFocus={() => {
                            this.setState({text: false})
                        }}
                        id="date"/>
                </div>
                <div>
                    <input type="submit" name="inscription" value="Suivant" className="myButton"
                           onClick={() => this.createUser()}/>

                    <div className="links">
                        <a onClick={() => this.props.signin()}>Vous avez un compte ? </a>

                    </div>
                </div>
            </div>

        );
    }


}
