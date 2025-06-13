import React, { useState } from 'react';
import './Register.css';

const Register = () => {
    const [form, setForm] = useState({
        username: '',
        firstName: '',
        lastName: '',
        password: '',
        rePassword: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (form.password !== form.rePassword) {
            alert("Passwords do not match!");
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/api/traders/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    username: form.username,
                    firstName: form.firstName,
                    lastName: form.lastName,
                    password: form.password
                }),
            });
            if (!response.ok) {
                const text = await response.text();
                throw new Error(`Register failed: ${response.status} - ${text}`);
            }
            alert('Registration successful! Redirecting to login...');
            window.location.href = '/login';
        } catch (error) {
            alert('Registration failed. Please check your data and try again.');
            console.error('Register error:', error);
        }
    };

    return (
        <div className="register-container">
            <form className="register-card" onSubmit={handleSubmit}>
                <h1 className="register-title">Register</h1>
                <div className="register-form-row">
                    <div className="register-form-left">
                        <div className="form-group">
                            <label htmlFor="username" className="register-label">
                                Username
                            </label>
                            <div className="input-icon-wrapper">
                                <i className="fa fa-user" aria-hidden="true"></i>
                                <input
                                    className='register-input'
                                    type="text"
                                    id="username"
                                    name="username"
                                    placeholder='username'
                                    required
                                    value={form.username}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="firstName" className="register-label">
                                First Name
                            </label>
                            <div className="input-icon-wrapper">
                                <i className="fa fa-id-card-o" aria-hidden="true"></i>
                                <input
                                    className='register-input'
                                    type="text"
                                    id="firstName"
                                    name="firstName"
                                    placeholder='first name'
                                    required
                                    value={form.firstName}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="lastName" className="register-label">
                                Last Name
                            </label>
                            <div className="input-icon-wrapper">
                                <i className="fa fa-id-card-o" aria-hidden="true"></i>
                                <input
                                    className='register-input'
                                    type="text"
                                    id="lastName"
                                    name="lastName"
                                    placeholder='last name'
                                    required
                                    value={form.lastName}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="register-form-right">
                        <div className="form-group">
                            <label htmlFor="password" className="register-label">
                                Password
                            </label>
                            <div className="input-icon-wrapper">
                                <i className="fa fa-lock" aria-hidden="true"></i>
                                <input
                                    className='register-input'
                                    type="password"
                                    id="password"
                                    name="password"
                                    placeholder='password'
                                    required
                                    value={form.password}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="rePassword" className="register-label">
                                Re-enter password
                            </label>
                            <div className="input-icon-wrapper">
                                <i className="fa fa-lock" aria-hidden="true"></i>
                                <input
                                    className='register-input'
                                    type="password"
                                    id="rePassword"
                                    name="rePassword"
                                    placeholder='re-password'
                                    required
                                    value={form.rePassword}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <button className='register-button' type="submit">Register</button>
                <div style={{ textAlign: 'center', marginTop: '1rem', fontSize: '1rem' }}>
                    Already have an account?{' '}
                    <a href="/login" className="login-link">
                        Click here to login
                    </a>
                </div>
            </form>
        </div>
    );
}

export default Register;
