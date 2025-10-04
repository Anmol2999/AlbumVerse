import { lazy } from 'react';

// project import
import Loadable from 'components/Loadable';
import MainLayout from 'layout/MainLayout';

// render - dashboard

// render - albums page
const AlbumsPage = Loadable(lazy(() => import('pages/albums/album')));
const AboutPage = Loadable(lazy(() => import('pages/Staticpages/about')));

// ==============================|| MAIN ROUTING ||============================== //

const MainRoutes = {
  path: '/',
  element: <MainLayout />,
  children: [
    {
      path: '/',
      element: <AlbumsPage />
    },
    {
      path: 'about',
      element: <AboutPage />
    }
  ]
};

export default MainRoutes;
