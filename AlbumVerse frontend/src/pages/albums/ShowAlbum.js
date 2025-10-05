import React from 'react';
import Header from './albums/header';
import PhotoGrid from './albums/photoGrid';

const ShowAlbum = () => {
  return (
    <div>
      <Header />
      <div style={{ marginTop: '20px', padding: '20px' }}></div>
      <PhotoGrid />
    </div>
  );
};

export default ShowAlbum;
