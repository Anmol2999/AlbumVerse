import React, { useEffect, useState } from 'react';
import {
  Grid,
  Card,
  CardMedia,
  CardContent,
  Typography,
  Tooltip,
  IconButton,
  Box,
  CircularProgress,
  Dialog,
  DialogContent,
  DialogActions,
  Button,
  useMediaQuery,
  Slide,
} from '@mui/material';
import {
  Download as DownloadIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as ViewIcon,
} from '@mui/icons-material';
import {
  fetchGetDataWithAuth,
  fetchGetDataWithAuthArrayBuffer,
  fetchDeleteDataWithAuth,
  fetchGetBlobDataWithAuth,
} from 'client/client';
import { useLocation } from 'react-router-dom';
import { Buffer } from 'buffer';
import { useTheme } from '@mui/material/styles';

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const PhotoGrid = () => {
  const [photos, setPhotos] = useState({});
  const [albumInfo, setAlbumInfo] = useState({});
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [currentPhoto, setCurrentPhoto] = useState(null);

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const album_id = queryParams.get('id');

  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const handleDownload = (downloadLink) => {
    fetchGetBlobDataWithAuth(downloadLink)
      .then((response) => {
        const disposition = response.headers['content-disposition'];
        const match = /filename="(.*?)"/.exec(disposition);
        const filename = match ? match[1] : 'downloadedFile';
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch((error) => console.error('Error downloading photo:', error));
  };

  const handleDelete = (photo_id) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this photo?');
    if (confirmDelete) {
      fetchDeleteDataWithAuth(`/albums/${album_id}/photos/${photo_id}/delete_photo`).then(() => {
        window.location.reload();
      });
    }
  };

  const handleView = (photo) => {
    setCurrentPhoto(photo);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setCurrentPhoto(null);
  };

  useEffect(() => {
    fetchGetDataWithAuth(`/albums/albums/${album_id}`).then((response) => {
      setAlbumInfo(response.data);
      const photoList = response.data.photos || [];

      Promise.all(
        photoList.map(async (photo) => {
          const imgResponse = await fetchGetDataWithAuthArrayBuffer(photo.downloadLink);
          const buffer = Buffer.from(imgResponse.data, 'binary').toString('base64');
          const key = `album_${album_id}_photo_${photo.id}`;
          return {
            key,
            photo_id: photo.id,
            name: photo.name,
            description: photo.description,
            content: buffer,
            downloadLink: photo.downloadLink,
          };
        })
      ).then((photoObjects) => {
        const photosMap = {};
        photoObjects.forEach((p) => {
          photosMap[p.key] = p;
        });
        setPhotos(photosMap);
        setLoading(false);
      });
    });
  }, [album_id]);

  if (loading)
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="80vh">
        <CircularProgress color="success" />
      </Box>
    );

  return (
    <Box sx={{ backgroundColor: '#f4f6f8', minHeight: '100vh', px: { xs: 2, sm: 5 }, py: 4 }}>
      {/* Album Header */}
      <Box mb={4}>
        <Typography
          variant="h4"
          sx={{ fontWeight: 700, color: '#1976d2', mb: 0.5, letterSpacing: 0.5 }}
        >
          {albumInfo.name}
        </Typography>
        <Typography variant="subtitle1" color="text.secondary" sx={{ maxWidth: '700px' }}>
          {albumInfo.description}
        </Typography>
      </Box>

      {/* Photo Grid */}
      <Grid container spacing={3}>
        {Object.keys(photos).map((key) => {
          const photo = photos[key];
          return (
            <Grid item xs={12} sm={6} md={4} lg={3} key={key}>
              <Card
                sx={{
                  borderRadius: 3,
                  overflow: 'hidden',
                  boxShadow: '0 8px 20px rgba(0,0,0,0.1)',
                  transition: 'all 0.3s ease',
                  '&:hover': { transform: 'translateY(-8px)', boxShadow: '0 12px 25px rgba(0,0,0,0.15)' },
                  display: 'flex',
                  flexDirection: 'column',
                  height: '100%',
                }}
              >
                <Box sx={{ position: 'relative' }}>
                  <CardMedia
                    component="img"
                    height="220"
                    image={`data:image/jpeg;base64,${photo.content}`}
                    alt={photo.description}
                    sx={{
                      transition: 'transform 0.5s ease',
                      '&:hover': { transform: 'scale(1.07)' },
                      cursor: 'pointer',
                    }}
                    onClick={() => handleView(photo)}
                  />
                  <Box
                    sx={{
                      position: 'absolute',
                      bottom: 0,
                      left: 0,
                      width: '100%',
                      background: 'linear-gradient(180deg, transparent, rgba(0,0,0,0.7))',
                      color: '#fff',
                      p: 1.5,
                    }}
                  >
                    <Tooltip title={photo.description}>
                      <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                        {photo.name}
                      </Typography>
                    </Tooltip>
                  </Box>
                </Box>

                <CardContent
                  sx={{
                    display: 'flex',
                    justifyContent: 'space-around',
                    backgroundColor: '#ffffff',
                    borderTop: '1px solid #e0e0e0',
                    py: 1,
                  }}
                >
                  <Tooltip title="View">
                    <IconButton
                      color="primary"
                      onClick={() => handleView(photo)}
                      sx={{
                        backgroundColor: '#e3f2fd',
                        '&:hover': { backgroundColor: '#bbdefb' },
                      }}
                    >
                      <ViewIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>

                  <Tooltip title="Edit">
                    <IconButton
                      color="info"
                      href={`/photo/edit?album_id=${album_id}&photo_id=${photo.photo_id}&photo_name=${photo.name}&photo_description=${photo.description}`}
                      sx={{
                        backgroundColor: '#e8f0fe',
                        '&:hover': { backgroundColor: '#c5d9fc' },
                      }}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>

                  <Tooltip title="Download">
                    <IconButton
                      color="secondary"
                      onClick={() => handleDownload(photo.downloadLink)}
                      sx={{
                        backgroundColor: '#f3e5f5',
                        '&:hover': { backgroundColor: '#e1bee7' },
                      }}
                    >
                      <DownloadIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>

                  <Tooltip title="Delete">
                    <IconButton
                      color="error"
                      onClick={() => handleDelete(photo.photo_id)}
                      sx={{
                        backgroundColor: '#ffebee',
                        '&:hover': { backgroundColor: '#ffcdd2' },
                      }}
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>
                </CardContent>
              </Card>
            </Grid>
          );
        })}
      </Grid>

      {/* Modern Modal */}
      <Dialog
        open={modalOpen}
        onClose={handleCloseModal}
        maxWidth="md"
        fullWidth
        fullScreen={fullScreen}
        TransitionComponent={Transition}
        PaperProps={{
          sx: { borderRadius: 3, backdropFilter: 'blur(8px)', backgroundColor: 'rgba(255,255,255,0.95)' },
        }}
      >
        <DialogContent sx={{ textAlign: 'center', p: 2 }}>
          {currentPhoto && (
            <img
              src={`data:image/jpeg;base64,${currentPhoto.content}`}
              alt={currentPhoto.description}
              style={{ maxWidth: '100%', maxHeight: fullScreen ? '60vh' : '75vh', borderRadius: '10px', boxShadow: '0 6px 20px rgba(0,0,0,0.2)' }}
            />
          )}
        </DialogContent>
        <DialogActions sx={{ justifyContent: 'center', pb: 2 }}>
          <Button
            variant="contained"
            color="secondary"
            startIcon={<DownloadIcon />}
            onClick={() => currentPhoto && handleDownload(currentPhoto.downloadLink)}
            sx={{ borderRadius: 3 }}
          >
            Download
          </Button>
          <Button
            variant="outlined"
            color="primary"
            onClick={handleCloseModal}
            sx={{ borderRadius: 3 }}
          >
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default PhotoGrid;
