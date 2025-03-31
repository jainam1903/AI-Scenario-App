import React from 'react';

const AnalysisResults = ({ results }) => {
  if (!results) return null;

  const { 
    scenarioSummary, 
    potentialPitfalls, 
    proposedStrategies, 
    recommendedResources, 
    disclaimer 
  } = results;

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4">Analysis Results</h2>
      
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-blue-700">Scenario Summary</h3>
        <p className="text-gray-700 mt-1">{scenarioSummary}</p>
      </div>
      
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-red-600">Potential Pitfalls</h3>
        <ul className="list-disc pl-5 mt-1">
          {potentialPitfalls.map((pitfall, index) => (
            <li key={index} className="text-gray-700 mb-1">{pitfall}</li>
          ))}
        </ul>
      </div>
      
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-green-600">Proposed Strategies</h3>
        <ul className="list-disc pl-5 mt-1">
          {proposedStrategies.map((strategy, index) => (
            <li key={index} className="text-gray-700 mb-1">{strategy}</li>
          ))}
        </ul>
      </div>
      
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-purple-600">Recommended Resources</h3>
        <ul className="list-disc pl-5 mt-1">
          {recommendedResources.map((resource, index) => (
            <li key={index} className="text-gray-700 mb-1">{resource}</li>
          ))}
        </ul>
      </div>
      
      <div className="mt-6 pt-4 border-t border-gray-200">
        <p className="text-sm text-gray-500 italic">{disclaimer}</p>
      </div>
    </div>
  );
};

export default AnalysisResults; 