import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL;
const fetchGetData = (uri) => {
  const url = `${BASE_URL}${uri}`;
  return axios.get(url).catch((error) => {
    console.error('Error fetching data for URL:', url, 'error', error);

    throw error;
  });
};

const fetchPostData = (uri, payload) => {
  const url = `${BASE_URL}${uri}`;
  return axios.post(url, payload).catch((error) => {
    //Handle exceptions/errors
    console.error('Error fetching data for URL:', url, 'error', error);
    //You can also throw the error if you want to handle it elsewhere
    throw error;
  });
};

export default fetchGetData;
export { fetchPostData };
