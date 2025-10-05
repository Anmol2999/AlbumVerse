import React, { useEffect, useState } from 'react';
import { Container, Grid, Card, CardContent, Typography, CircularProgress, Box, Fab, Tooltip } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import PhotoAlbumIcon from '@mui/icons-material/PhotoAlbum';
import { useNavigate } from 'react-router-dom';
import { fetchGetDataWithAuth } from 'client/client';

// Modern gradients for cards
const gradients = [
  'linear-gradient(145deg, #FFD3B6 0%, #FFAAA5 100%)',
  'linear-gradient(145deg, #D5AAFF 0%, #85C1E9 100%)',
  'linear-gradient(145deg, #A8FF98 0%, #56CCF2 100%)',
  'linear-gradient(145deg, #FFEE93 0%, #FFB86C 100%)',
  'linear-gradient(145deg, #FFA69E 0%, #FF6A88 100%)',
  'linear-gradient(145deg, #43C6AC 0%, #F8FFAE 100%)',
  'linear-gradient(145deg, #6A82FB 0%, #FC5C7D 100%)',
  'linear-gradient(145deg, #FBC2EB 0%, #A18CD1 100%)'
];

const getRandomGradient = () => gradients[Math.floor(Math.random() * gradients.length)];

// Particle colors for background
const particleColors = [
  '#FFD3B6',
  '#FFAAA5',
  '#D5AAFF',
  '#85C1E9',
  '#A8FF98',
  '#56CCF2',
  '#FFEE93',
  '#FFB86C',
  '#FFA69E',
  '#861657',
  '#00C9FF',
  '#92FE9D',
  '#F5AF19',
  '#F12711',
  '#43C6AC',
  '#F8FFAE',
  '#6A82FB',
  '#FC5C7D',
  '#FBD3E9',
  '#BB377D',
  '#FF9A8B',
  '#FF6A88',
  '#FF99AC',
  '#89F7FE',
  '#66A6FF',
  '#FBC2EB',
  '#A18CD1'
];

const Particle = ({ size, top, left, color, duration, delay }) => (
  <Box
    sx={{
      position: 'absolute',
      width: size,
      height: size,
      top,
      left,
      backgroundColor: color,
      borderRadius: '50%',
      opacity: 0.15,
      animation: `particleMove ${duration}s ease-in-out infinite`,
      animationDelay: `${delay}s`,
      zIndex: 0
    }}
  />
);

const AlbumsPage = () => {
  const [albums, setAlbums] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleClick = (albumId) => {
    navigate(`/album/show?id=${albumId}`);
    window.location.reload();
  }

  useEffect(() => {
    const fetchAlbums = async () => {
      try {
        const response = await fetchGetDataWithAuth('/albums/albums');
        if (response.status === 200) setAlbums(response.data);
        else setError('Failed to load albums.');
      } catch (err) {
        setError(err.message.includes('No auth token') ? 'Please login to view your albums.' : 'An error occurred while fetching albums.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchAlbums();
  }, []);

  if (loading)
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
        <CircularProgress size={50} />
      </Box>
    );

  if (error)
    return (
      <Container maxWidth="sm">
        <Box mt={10} p={4} borderRadius={4} boxShadow={3} bgcolor="rgba(255,255,255,0.85)" textAlign="center" backdropFilter="blur(8px)">
          <Typography variant="h6" color="error">
            {error}
          </Typography>
        </Box>
      </Container>
    );

  return (
    <Box
      sx={{
        py: 8,
        minHeight: '100vh',
        position: 'relative',
        overflow: 'hidden',
        background: 'linear-gradient(120deg, #fdfbfb 0%, #ebedee 100%)'
      }}
    >
      {/* Floating particles */}
      {Array.from({ length: 30 }).map((_, i) => (
        <Particle
          key={i}
          size={`${Math.random() * 15 + 10}px`}
          top={`${Math.random() * 100}%`}
          left={`${Math.random() * 100}%`}
          color={particleColors[Math.floor(Math.random() * particleColors.length)]}
          duration={Math.random() * 20 + 15}
          delay={Math.random() * 5}
        />
      ))}

      <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
        {/* Header */}
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={6}>
          <Typography variant="h4" fontWeight="bold" sx={{ color: '#1f2937', textShadow: '1px 1px 2px rgba(0,0,0,0.1)' }}>
            My Albums
          </Typography>
          <Tooltip title="Add New Album">
            <Fab
              onClick={() => navigate(`/album/add`)}
              sx={{
                background: 'linear-gradient(135deg, #6D5BFF 0%, #8B9EFF 100%)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #5B4BEE 0%, #7A8DEE 100%)',
                  transform: 'scale(1.05)'
                },
                boxShadow: '0 8px 20px rgba(43, 36, 36, 0.3)'
              }}
            >
              <AddIcon sx={{ fontSize: 28 }} />
            </Fab>
          </Tooltip>
        </Box>

        {/* No albums */}
        {albums.length === 0 ? (
          <Typography align="center" color="text.secondary" sx={{ mt: 10, fontStyle: 'italic', fontSize: '1.1rem' }}>
            No albums found. Click the &quot;+&quot; button above to create one.
          </Typography>
        ) : (
          <Grid container spacing={4}>
            {albums.map((album) => (
              <Grid item xs={12} sm={6} md={4} lg={3} key={album.id}>
                <Card
                  onClick={() => handleClick(album.id)}
                  sx={{
                    borderRadius: 4,
                    cursor: 'pointer',
                    background: getRandomGradient(),
                    position: 'relative',
                    overflow: 'hidden',
                    boxShadow: '0 8px 24px rgba(0,0,0,0.2), 0 6px 12px rgba(0,0,0,0.1)',
                    transition: 'all 0.5s ease',
                    '&:hover': {
                      transform: 'translateY(-10px) scale(1.03)',
                      boxShadow: '0 12px 40px rgba(0,0,0,0.25), 0 8px 20px rgba(0,0,0,0.15)'
                    }
                  }}
                >
                  <CardContent
                    sx={{
                      position: 'relative',
                      zIndex: 1,
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}
                  >
                    {/* Album Icon */}
                    <Box
                      sx={{
                        width: 60,
                        height: 60,
                        borderRadius: '50%',
                        backgroundColor: 'rgba(255,255,255,0.3)',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        mb: 2,
                        boxShadow: '0 4px 15px rgba(0,0,0,0.1)',
                        transition: 'all 0.4s ease',
                        '&:hover': { transform: 'scale(1.15)', boxShadow: '0 8px 25px rgba(0,0,0,0.2)' }
                      }}
                    >
                      <PhotoAlbumIcon sx={{ fontSize: 30, color: '#fff' }} />
                    </Box>

                    <Typography
                      variant="h6"
                      fontWeight="bold"
                      gutterBottom
                      sx={{
                        color: '#1f2937',
                        textAlign: 'center',
                        textShadow: '0px 1px 2px rgba(0,0,0,0.1)'
                      }}
                    >
                      {album.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ textAlign: 'center' }}>
                      {album.description || 'No description provided.'}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Container>

      <style>
        {`
          @keyframes particleMove {
            0% { transform: translateY(0px); }
            50% { transform: translateY(-20px); }
            100% { transform: translateY(0px); }
          }
        `}
      </style>
    </Box>
  );
};

export default AlbumsPage;
