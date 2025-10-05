import { useTheme } from '@mui/material/styles';
import { useState } from 'react';

const Logo = () => {
  const theme = useTheme();
  const [hover, setHover] = useState(false);

  return (
    <svg
      width="200"
      height="50"
      viewBox="0 0 250 50"
      xmlns="http://www.w3.org/2000/svg"
      style={{ cursor: 'pointer' }}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
    >
      {/* Gradient Definition */}
      <defs>
        <linearGradient id="logoGradient" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stopColor={theme.palette.primary.light} />
          <stop offset="100%" stopColor={theme.palette.primary.main} />
        </linearGradient>
        <filter id="shadow" x="-20%" y="-20%" width="140%" height="140%">
          <feDropShadow dx="2" dy="2" stdDeviation="2" floodColor="#000" floodOpacity="0.3"/>
        </filter>
      </defs>

      {/* Album Icon */}
      <g
        transform={hover ? 'scale(1.1) rotate(5 15 20)' : 'scale(1) rotate(0 15 20)'}
        style={{ transition: 'transform 0.3s ease' }}
      >
        <rect
          x="0"
          y="5"
          width="30"
          height="30"
          rx="5"
          ry="5"
          fill={theme.palette.primary.main}
        />
        <circle
          cx="15"
          cy="20"
          r="7"
          fill={theme.palette.background.paper}
        />
        <rect
          x="5"
          y="15"
          width="20"
          height="10"
          fill={theme.palette.primary.light}
          rx="2"
          ry="2"
        />
      </g>

      {/* Text with Gradient and Shadow */}
      <text
        x="40"
        y="35"
        fontFamily="'Segoe UI', Arial, sans-serif"
        fontSize="32"
        fontWeight="700"
        fill={hover ? theme.palette.primary.dark : 'url(#logoGradient)'}
        filter="url(#shadow)"
        style={{ transition: 'fill 0.3s ease' }}
      >
        AlbumVerse
      </text>
    </svg>
  );
};

export default Logo;
  