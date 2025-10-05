import React from 'react';
import { Link } from 'react-router-dom';
import { Button, AppBar, Toolbar, Typography } from '@mui/material';
import { useLocation } from 'react-router-dom';
import { fetchDeleteDataWithAuth } from 'client/client';

const Header = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');

  const handleDelete = () => {
    console.log('Album deleted');
    fetchDeleteDataWithAuth('/albums/' + id + '/delete_album').then((response) => {
      console.log(response);
      window.location.href = '/';
    });
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Photo Gallery
        </Typography>
        <Button
          color="inherit"
          component={Link}
          to={`/album/edit?id=${id}`}
          variant="contained"
          sx={{ mr: 2, '&:hover': { backgroundColor: '#2f6ad0' }, backgroundColor: '#799edc' }}
        >
          Edit Album
        </Button>
        <Button
          color="inherit"
          component={Link}
          to={`/album/upload?id=${id}`}
          variant="contained"
          sx={{ mr: 2, '&:hover': { backgroundColor: '#388E3C' }, backgroundColor: '#4CAF50' }}
        >
          Upload Photo
        </Button>
        <Button
          color="inherit"
          onClick={handleDelete}
          variant="contained"
          sx={{ mr: 2, '&:hover': { backgroundColor: '#f44336' }, backgroundColor: '#d32f2f' }}
        >
          Delete Album
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
