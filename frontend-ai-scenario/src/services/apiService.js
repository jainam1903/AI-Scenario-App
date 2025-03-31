import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// For debugging
console.log('Using API URL:', API_BASE_URL);

// Create an axios instance with common config
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: false,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Configure axios interceptors
axiosInstance.interceptors.request.use(
  config => {
    console.log('Making request to:', config.url);
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  response => {
    console.log('Response received:', response.status);
    return response;
  },
  error => {
    console.error('Response error:', error.message);
    if (error.response) {
      console.error('Error details:', error.response.data);
    }
    return Promise.reject(error);
  }
);

const apiService = {
  testConnection: async () => {
    try {
      const response = await axiosInstance.get('/test');
      return response.data;
    } catch (error) {
      console.error('Error testing connection:', error);
      throw error;
    }
  },
  
  testOpenAi: async () => {
    try {
      const response = await axiosInstance.get('/test-openai');
      return response.data;
    } catch (error) {
      console.error('Error testing OpenAI:', error);
      throw error;
    }
  },
  
  analyzeScenario: async (scenarioData) => {
    try {
      console.log('Sending scenario data:', scenarioData);
      const response = await axiosInstance.post('/analyze-scenario', scenarioData);
      console.log('Received analysis response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error analyzing scenario:', error);
      throw error;
    }
  }
};

export default apiService; 