import React, { useState } from 'react';
import Header from './albums/header';
import { Button, Typography, Paper, Container, IconButton, Grid } from '@mui/material';
import { AddCircleOutline, Close } from '@mui/icons-material';
import { useDropzone } from 'react-dropzone';
import { useLocation, useNavigate } from 'react-router-dom';
import { fetchPostFileUploadWithAuth } from '../../client/client';
import { CircularProgress } from '@mui/material';
import { Box } from '../../../node_modules/@mui/material/index';

const FileUploadPage = () => {
  const [files, setFiles] = useState([]);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');
  const navigate = useNavigate();
  const [processing, setProcessing] = useState(false);

  const onDrop = (acceptedFiles) => {
    setFiles((prevFiles) => [...prevFiles, ...acceptedFiles]);
  };

  const { getRootProps, getInputProps } = useDropzone({ onDrop, multiple: true });

  const removeFile = (index) => {
    setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
  };

  const handleUpload = async () => {
    try {
      setProcessing(true);
      const formData = new FormData();
      files.forEach((file) => {
        formData.append('files', file);
      });

      fetchPostFileUploadWithAuth('/albums/' + id + '/upload-photos', formData).then((response) => {
        console.log('Upload response:', response.data);
        navigate('/album/show?id=' + id);
      });

      setFiles([]);
    } catch (err) {
      console.error('Upload error:', err);
    }
  };

  return (
    <div>
      <Header />
      <Container>
        <Paper elevation={3} sx={{ p: 4, mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Typography variant="h4" align="center" gutterBottom>
                Upload Photos
              </Typography>
            </Grid>

            {/* Dropzone */}
            <Grid item xs={12} {...getRootProps()}>
              <input {...getInputProps()} />
              <Paper
                elevation={2}
                sx={{
                  border: '2px dashed #1976d2',
                  borderRadius: 2,
                  p: 4,
                  textAlign: 'center',
                  cursor: 'pointer',
                  transition: '0.3s',
                  '&:hover': { backgroundColor: '#f0f4ff' }
                }}
              >
                <AddCircleOutline fontSize="large" color="primary" />
                <Typography variant="h6" sx={{ mt: 1 }}>
                  Drag & drop files here, or click to select files
                </Typography>
              </Paper>
            </Grid>

            {/* File Preview */}
            <Grid item xs={12}>
              {files.map((file, index) => (
                <Paper
                  key={index}
                  elevation={1}
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    p: 2,
                    mt: 2,
                    border: '1px solid #ccc',
                    borderRadius: 1
                  }}
                >
                  <Typography variant="body1">{file.name}</Typography>
                  <IconButton onClick={() => removeFile(index)} color="error">
                    <Close />
                  </IconButton>
                </Paper>
              ))}
            </Grid>

            {/* Upload Button */}
            <Grid item xs={12} sx={{ textAlign: 'center' }}>
              {processing ? (
                <Box textAlign="center">
                  <CircularProgress />
                  <Typography variant="body2" color="textSecondary" marginTop="10px">
                    Uploading...
                  </Typography>
                </Box>
              ) : (
                <Button variant="contained" disabled={files.length === 0} color="primary" onClick={handleUpload}>
                  Upload Photos
                </Button>
              )}
            </Grid>
          </Grid>
        </Paper>
      </Container>
    </div>
  );
};

export default FileUploadPage;
