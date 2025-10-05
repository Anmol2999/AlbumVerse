import axios from 'axios';

const BASE_URL = process.env.REACT_APP_BASE_URL;
const fetchGetData = (uri) => {
  const url = `${BASE_URL}${uri}`;
  return axios.get(url).catch((error) => {
    console.error('Error fetching data for URL:', url, 'error', error);

    throw error;
  });
};

const fetchPostData = (uri, payload, headers) => {
  const url = `${BASE_URL}${uri}`;
  return axios.post(url, payload, { headers }).catch((error) => {
    //Handle exceptions/errors
    console.error('Error fetching data for URL:', url, 'error', error);
    //You can also throw the error if you want to handle it elsewhere
    throw error;
  });
};

const fetchPostDataWithAuth = (uri, payload, headers) => {
  const url = `${BASE_URL}${uri}`;
  return axios.post(url, payload, { headers }).catch((error) => {
    //Handle exceptions/errors
    console.error('Error fetching data for URL:', url, 'error', error);
    //You can also throw the error if you want to handle it elsewhere
    throw error;
  });
};

const fetchGetDataWithAuth = async (uri) => {
  const url = `${BASE_URL}${uri}`;
  const token = localStorage.getItem('authToken');
  if (!token) {
    throw new Error('No auth token found');
  }

  try {
    const response = await axios.get(url, {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'error', error);
    throw error;
  }
};

const fetchPostFileUploadWithAuth = async (uri, formData) => {
  const url = `${BASE_URL}${uri}`;
  const token = localStorage.getItem('authToken');
  try {
    const response = await axios.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${token}`
      }
    });
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'error', error);
    throw error;
  }
};

const fetchGetDataWithAuthArrayBuffer = (uri) => {
  const url = `${BASE_URL}${uri}`;
  const token = localStorage.getItem('authToken');
  if (!token) {
    throw new Error('No auth token found');
  }

  try {
    const response = axios.get(url, {
      headers: { Authorization: `Bearer ${token}` },
      responseType: 'arraybuffer'
    });
    return response;
  } catch (error) {
    console.error('Error fetching data ', error);
    throw error;
  }
};
const fetchPutDataWithAuth = (uri, payload, headers) => {
  const url = `${BASE_URL}${uri}`;
  return axios.put(url, payload, { headers }).catch((error) => {
    //Handle exceptions/errors
    console.error('Error fetching data for URL:', url, 'error', error);
    //You can also throw the error if you want to handle it elsewhere
    throw error;
  });
};

const fetchDeleteDataWithAuth = (uri) => {
  const url = `${BASE_URL}${uri}`;
  const token = localStorage.getItem('authToken');
  if (!token) {
    throw new Error('No auth token found');
  }

  try {
    const response = axios.delete(url, {
      headers: { "Authorization": `Bearer ${token}` }
    });
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'error', error);
    //You can also throw the error if you want to handle it elsewhere
    throw error;
  }
};


const fetchGetBlobDataWithAuth = async (uri) => {
  const url = `${BASE_URL}${uri}`;
  const token = localStorage.getItem('authToken');

  try {
    const response = await axios.get(url, {
      headers: { Authorization: `Bearer ${token}` },
      responseType: 'blob'
    });
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'error', error);
    throw error;
  }
};
export default fetchGetData;
export {
  fetchPostData,
  fetchPostDataWithAuth,
  fetchGetDataWithAuth,
  fetchPostFileUploadWithAuth,
  fetchGetDataWithAuthArrayBuffer,
  fetchPutDataWithAuth,
  fetchDeleteDataWithAuth,
  fetchGetBlobDataWithAuth
};
