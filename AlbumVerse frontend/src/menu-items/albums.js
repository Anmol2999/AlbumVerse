// assets
import { PictureOutlined } from '@ant-design/icons';

// icons
const icons = {
  PictureOutlined
};

// ==============================|| MENU ITEMS - SAMPLE PAGE & DOCUMENTATION ||============================== //

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
    }
  ]
};

export default albums;
