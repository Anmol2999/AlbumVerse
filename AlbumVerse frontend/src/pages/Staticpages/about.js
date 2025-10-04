import { Typography } from '@mui/material';
import MainCard from 'components/MainCard';

const AboutPage = () => {
  return (
    <MainCard title="About">
      <Typography variant="body2">
        AlbumVerse is a smart photo album management and organizing system designed to help you keep your memories safe, structured, and
        easily accessible. Whether it is family photos, travel diaries, event collections, or professional portfolios, AlbumVerse makes
        storing and managing images effortless.
      </Typography>
    </MainCard>
  );
};

export default AboutPage;
