import { lazy } from 'react';

// project import
import Loadable from 'components/Loadable';
import MainLayout from 'layout/MainLayout';

// render - dashboard

// render - albums page
const AlbumsPage = Loadable(lazy(() => import('pages/albums/album')));
const AddAlbumPage = Loadable(lazy(() => import('pages/albums/AddAlbum')));
  const ShowAlbumPage = Loadable(lazy(() => import('pages/albums/ShowAlbum')));
  const EditAlbumPage = Loadable(lazy(() => import('pages/albums/EditAlbum')));
const UploadPhotoPage = Loadable(lazy(() => import('pages/albums/UploadPhoto')));
const EditPhotoPage = Loadable(lazy(() => import('pages/albums/EditPhoto')));

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
      path: '/album/add',
      element: <AddAlbumPage />
    },

    {
      path: '/album/show',
      element: <ShowAlbumPage />
    },
    {
      path: '/album/edit',
      element: <EditAlbumPage />
    },
     {
      path: '/photo/edit',
      element: <EditPhotoPage />
    },
    {
      path: '/album/upload',
      element: <UploadPhotoPage />
    },
    {
      path: 'about',
      element: <AboutPage />
    }
  ]
};

export default MainRoutes;
