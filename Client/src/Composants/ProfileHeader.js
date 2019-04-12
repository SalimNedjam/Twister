import React, {Component} from "react";
import profile from '../res/profile.png'
import {read_cookie} from "sfcookies";
import axios from 'axios'

export default class ProfileHeader extends Component {

    constructor(props) {
        super(props);

        this.state = {
            clicked: false,
            file: null,

        };

        this.uploadImage = this.uploadImage.bind(this);
        this.onChange = this.onChange.bind(this);

    }


    uploadImage() {

        if (this.state.file !== null) {

            let reader = new FileReader();
            reader.onload = (e) => {
                const formData = new FormData();
                formData.append('photo', e.target.result);
                formData.append('key', read_cookie("key"));

                // noinspection JSUnusedLocalSymbols
                axios.post("http://localhost:8080/TwisterFinal/Image", formData)
                    .then((response) => {
                        this.setState({
                            file: null
                        });
                        this.props.reload();
                    }).catch((error) => {
                });

            };
            reader.readAsDataURL(this.state.file);


        }
    }

    componentDidMount() {
        const params = new URLSearchParams();
        params.append('key', read_cookie("key"));
        params.append('id', this.props.id);

        axios.get("http://localhost:8080/TwisterFinal/Image?" + params)
            .then(res => {
                this.setState({
                    image: res.data
                })
            });
    }


    onError() {
        this.setState({
            image: profile
        })
    }


    onChange(event) {
        this.setState({
            file: event.target.files[0]
        });
        if (event.target.files && event.target.files[0]) {
            let reader = new FileReader();
            reader.onload = (e) => {

                this.setState({
                    image: e.target.result
                });
            };
            reader.readAsDataURL(event.target.files[0]);
            console.log(event.target.files[0])
        }

    }

    componentWillReceiveProps(nextProps) {

        const params = new URLSearchParams();
        params.append('key', read_cookie("key"));
        params.append('id', nextProps.id);

        axios.get("http://localhost:8080/TwisterFinal/Image?" + params)
            .then(res => {
                this.setState({
                    image: res.data
                })
            });

    }

    render() {


        return (<div className="ProfileInfos">
            <label htmlFor="input-file" className="label-file">
                <img className="photo" onError={this.onError.bind(this)} src={this.state.image}/>

            </label>
            <div className="infos">
                <div className="name">
                    {this.props.prenom} {this.props.nom}
                </div>
                <div className="username">
                    @{this.props.login}
                </div>

            </div>
            {this.props.holder !== undefined &&
            <div className="upload">

                <input className="input-file" id="input-file" type="file" name="myImage" accept="image/*"
                       onChange={(e) => this.onChange(e)}/>
                {
                    this.state.file &&
                    <a onClick={(e) => this.uploadImage()}>Upload Image</a>

                }
            </div>
            }
        </div>);


    }


}
