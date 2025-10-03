import { useState } from 'react';
import { Button, Container, TextField } from '@mui/material';
import { fetchPostData } from 'client/client';

const AuthLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  //validation errors
  const [errors, setErrors] = useState({ email: '', password: '' });
  const [loginError, setLoginError] = useState('');

  const validateEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePassword = () => {
    return password.length >= 6 && password.length <= 20;
  };

  const handleLogin = async () => {
    //Reset previous errors
    setErrors({ email: '', password: '' });

    if (!validateEmail()) {
      setErrors((prevErrors) => ({ ...prevErrors, email: 'Invalid email format' }));
      return;
    }

    if (!validatePassword()) {
      setErrors((prevErrors) => ({ ...prevErrors, password: 'Password must be between 6 and 20 characters' }));
      return;
    }
    // If validation passes, proceed with login
    fetchPostData('/auth/token', { email, password })
      .then((response) => {
        const { token } = response.data;
        setLoginError('');
        // Store the token in localStorage or context
        localStorage.setItem('authToken', token);
        // Redirect or update UI after successful login
        
      })
      .catch((error) => {
        console.error('Login failed:', error);
        //handle other login error
        setLoginError('Login failed. Please check your credentials and try again.');
      });
  };

  return (
    <Container component="main" maxWidth="xs">
      <TextField
        variant="outlined"
        margin="normal"
        fullWidth
        label="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        error={!!errors.email}
        helperText={errors.email}
      />
      <TextField
        variant="outlined"
        margin="normal"
        fullWidth
        label="password"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        error={!!errors.password}
        helperText={errors.password}
      />
      <Button variant="contained" color="primary" fullWidth onClick={handleLogin}>
        Login
      </Button>
      {loginError && <p style={{ color: 'red' }}>{loginError}</p>}
    </Container>
  );
};

export default AuthLogin;
