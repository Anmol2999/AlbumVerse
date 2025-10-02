import axios from 'axios';

const fetchGetData = (url) => {
    return axios.get(url).catch((error) => {
        console.error('Error fetching data for URL:', url, 'error', error);

        throw error;
    });
};


export default fetchGetData;