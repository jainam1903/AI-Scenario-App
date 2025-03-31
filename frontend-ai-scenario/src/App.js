import React, { useState } from 'react';
import ScenarioForm from './components/ScenarioForm';
import AnalysisResults from './components/AnalysisResults';
import apiService from './services/apiService';
import './App.css';

function App() {
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [testMessage, setTestMessage] = useState(null);

  const handleSubmit = async (scenarioData) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await apiService.analyzeScenario(scenarioData);
      setResults(response);
    } catch (err) {
      setError('An error occurred while analyzing your scenario. Please try again.');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };
  
  const testApi = async () => {
    setTestMessage("Testing API connection...");
    try {
      const result = await apiService.testConnection();
      setTestMessage(`API Connection test: ${result}`);
    } catch (err) {
      setTestMessage(`API Connection error: ${err.message}`);
    }
  };
  
  const testOpenAi = async () => {
    setTestMessage("Testing OpenAI connection...");
    try {
      const result = await apiService.testOpenAi();
      setTestMessage(`OpenAI test: ${result}`);
    } catch (err) {
      setTestMessage(`OpenAI test error: ${err.message}`);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 py-10">
      <div className="container mx-auto px-4 max-w-4xl">
        <header className="mb-10 text-center">
          <h1 className="text-3xl font-bold text-gray-800">AI Scenario Analysis</h1>
          <p className="text-gray-600 mt-2">
            Enter your scenario and constraints for AI-powered analysis and recommendations
          </p>
          <div className="mt-4 flex justify-center space-x-4">
            <button 
              onClick={testApi}
              className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
            >
              Test API Connection
            </button>
            <button 
              onClick={testOpenAi}
              className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600"
            >
              Test OpenAI
            </button>
          </div>
          {testMessage && (
            <div className="mt-2 text-sm font-medium text-gray-700">
              {testMessage}
            </div>
          )}
        </header>

        <div className="grid md:grid-cols-1 gap-8">
          <ScenarioForm onSubmit={handleSubmit} isLoading={loading} />
          
          {loading && (
            <div className="text-center p-4">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500 mx-auto"></div>
              <p className="mt-2 text-gray-600">Analyzing your scenario...</p>
            </div>
          )}
          
          {error && (
            <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4">
              <p>{error}</p>
            </div>
          )}
          
          {results && !loading && <AnalysisResults results={results} />}
        </div>
      </div>
    </div>
  );
}

export default App;
