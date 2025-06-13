import React, { useState } from 'react';
import './Login.css'
import type Trader from '../interfaces/Trader';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
          const response = await fetch('http://localhost:8080/api/traders/login', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
          });

          if (!response.ok) {
            const text = await response.text();
            throw new Error(`Login failed: ${response.status} - ${text}`);
          }

          const trader: Trader = await response.json();
          document.cookie = `traderId=${trader.id}; path=/;`;
          document.cookie = `traderName=${trader.fullName}; path=/;`;
          document.cookie = `traderUsername=${trader.username}; path=/;`;

          window.location.href = '/market';
        } catch (error) {
          alert('Login failed. Please check your credentials and try again.');
          console.error('Login error:', error);
        }
    };

    return (
        <div className="login-container">
            <form className="login-card" onSubmit={handleSubmit}>
                <h1 className="login-title">Login</h1>
                <div className="form-group">
                    <label htmlFor="username" className="login-label">
                        Username
                    </label>
                    <div className="input-icon-wrapper">
                        <i className="fa fa-user" aria-hidden="true"></i>
                        <input
                            className='login-input'
                            type="text"
                            id="username"
                            name="username"
                            placeholder='username'
                            required
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                        />
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="password" className="login-label">
                        Password
                    </label>
                    <div className="input-icon-wrapper">
                        <i className="fa fa-lock" aria-hidden="true"></i>
                        <input
                            className='login-input'
                            type="password"
                            id="password"
                            name="password"
                            placeholder='password'
                            required
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                        />
                    </div>
                </div>
                <button className='login-button' type="submit">Login</button>
                <div style={{ textAlign: 'center', marginTop: '1rem', fontSize: '1rem' }}>
                    Don&apos;t have an account yet?{' '}
                    <a href="/register" className="register-link">
                        Click here to register
                    </a>
                </div>
            </form>
        </div>
    );
}

export default Login;
