import { Box, Button, Container, TextField, Typography } from '@mui/material';
import { fetchGetDataWithAuth, fetchPutDataWithAuth } from 'client/client';
import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const EditAlbum = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [errors, setErrors] = useState({ name: '', description: '' });
  const [submitError, setSubmitError] = useState('');
  const [submitSuccess, setSubmitSuccess] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const album_id = queryParams.get('id');

  useEffect(() => {
    // Fetch existing album data to pre-fill the form
    fetchGetDataWithAuth('/albums/albums/' + album_id).then((response) => {
      const albumData = response.data;
      setName(albumData.name);
      setDescription(albumData.description);
    });
  }, [album_id]);

  const validateInputs = () => {
    let valid = true;
    const newErrors = { name: '', description: '' };

    if (!name.trim()) {
      newErrors.name = 'Album name is required';
      valid = false;
    }

    if (description.trim().length < 10) {
      newErrors.description = 'Description must be at least 10 characters';
      valid = false;
    }

    setErrors(newErrors);
    return valid;
  };

  const handleSubmit = async () => {
    if (!validateInputs()) return;

    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        setSubmitError('Please login before updating an album.');
        return;
      }

      const response = await fetchPutDataWithAuth(
        '/albums/' + album_id + '/update_album',
        { name, description },
        { accept: '*/*', 'Content-Type': 'application/json', Authorization: `Bearer ${token}` }
      );

      if (response.status === 200 || response.status === 201) {
        setSubmitSuccess('Album updated successfully!');
        setName('');
        setDescription('');
        setTimeout(() => navigate('/'), 1500); // redirect after success
      } else {
        setSubmitError('Failed to update album. Try again.');
      }
    } catch (error) {
      console.error(error);
      setSubmitError('Something went wrong while updating the album.');
    }
  };

  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          mt: 8,
          p: 4,
          boxShadow: 3,
          borderRadius: 2,
          backgroundColor: 'background.paper'
        }}
      >
        <Typography variant="h5" align="center" gutterBottom>
          Edit Album
        </Typography>

        <TextField
          label="Album Name"
          variant="outlined"
          fullWidth
          margin="normal"
          value={name}
          onChange={(e) => setName(e.target.value)}
          error={!!errors.name}
          helperText={errors.name}
        />

        <TextField
          label="Description"
          variant="outlined"
          fullWidth
          margin="normal"
          multiline
          rows={4}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          error={!!errors.description}
          helperText={errors.description}
        />

        <Button variant="contained" color="primary" fullWidth sx={{ mt: 2 }} onClick={handleSubmit}>
          Edit Album
        </Button>

        {submitError && (
          <Typography color="error" sx={{ mt: 2 }}>
            {submitError}
          </Typography>
        )}
        {submitSuccess && (
          <Typography color="primary" sx={{ mt: 2 }}>
            {submitSuccess}
          </Typography>
        )}
      </Box>
    </Container>
  );
};

export default EditAlbum;
