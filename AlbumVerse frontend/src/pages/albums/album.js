import { Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import MainCard from 'components/MainCard';

const SamplePage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const isLoggedIn = localStorage.getItem('authToken');
    if (!isLoggedIn) {
      navigate('/login');
      window.location.reload();
    }
  });

  return (
    <MainCard title="Albums">
      <Typography variant="body2">Albums</Typography>
    </MainCard>
  );
};

export default SamplePage;
