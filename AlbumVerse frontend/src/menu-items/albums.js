// assets
import { PictureOutlined, FileImageOutlined } from '@ant-design/icons';

// icons
const icons = {
  PictureOutlined,
  FileImageOutlined
};

const albums = {
  id: 'albums',
  title: 'Home',
  type: 'group',
  children: [
    {
      id: 'Albums',
      title: 'Albums',
      type: 'item',
      url: '/',
      icon: icons.PictureOutlined
    },
    {
      id: 'AddAlbums',
      title: 'Add Albums',
      type: 'item',
      url: '/album/add',
      icon: icons.FileImageOutlined
    }
  ]
};

export default albums;
