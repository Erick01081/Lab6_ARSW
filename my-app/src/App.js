import React, { useState, useEffect, useRef, useCallback } from 'react';
import styled from 'styled-components';
import Modal from 'react-modal';
import { FaSearch, FaDraftingCompass } from 'react-icons/fa';

const Container = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
`;

const Title = styled.h1`
  color: #2c3e50;
  text-align: center;
`;

const InputGroup = styled.div`
  display: flex;
  margin-bottom: 20px;
`;

const Input = styled.input`
  flex: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #bdc3c7;
  border-radius: 4px 0 0 4px;
`;

const Button = styled.button`
  padding: 10px 20px;
  font-size: 16px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #2980b9;
  }
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
`;

const Th = styled.th`
  background-color: #34495e;
  color: white;
  padding: 12px;
  text-align: left;
`;

const Td = styled.td`
  border: 1px solid #ecf0f1;
  padding: 12px;
`;

const TotalPoints = styled.p`
  font-weight: bold;
  font-size: 18px;
  color: #2c3e50;
`;

const ModalContent = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const CanvasContainer = styled.div`
  margin-top: 20px;
  border: 1px solid #bdc3c7;
  border-radius: 4px;
  overflow: hidden;
`;

Modal.setAppElement('#root');

function BlueprintViewer() {
  const [author, setAuthor] = useState('');
  const [blueprints, setBlueprints] = useState([]);
  const [selectedBlueprint, setSelectedBlueprint] = useState(null);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const canvasRef = useRef(null);

  const fetchBlueprints = async () => {
    try {
      const url = author ? `http://localhost:8080/blueprints/${author}` : 'http://localhost:8080/blueprints';
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch blueprints');
      const data = await response.json();
      setBlueprints(data);
      setSelectedBlueprint(null);
    } catch (error) {
      console.error('Error fetching blueprints:', error);
      setBlueprints([]);
    }
  };

  const drawBlueprint = useCallback((blueprint) => {
    console.log("Attempting to draw blueprint:", blueprint);
    const canvas = canvasRef.current;
    if (!canvas || !blueprint) {
      console.log("Canvas or blueprint not available");
      return;
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.log("2D context not available");
      return;
    }

    console.log("Canvas dimensions:", canvas.width, canvas.height);
    console.log("Blueprint points:", blueprint.points);

    // Clear the canvas before drawing
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Check if the blueprint has at least one point
    if (blueprint.points.length === 0) {
      console.log("Blueprint has no points");
      return;
    }

    // Calculate the scale to fit the blueprint in the canvas
    const margin = 20;
    const xValues = blueprint.points.map(p => p.x);
    const yValues = blueprint.points.map(p => p.y);
    const minX = Math.min(...xValues);
    const maxX = Math.max(...xValues);
    const minY = Math.min(...yValues);
    const maxY = Math.max(...yValues);
    const scaleX = (canvas.width - 2 * margin) / (maxX - minX || 1);
    const scaleY = (canvas.height - 2 * margin) / (maxY - minY || 1);
    const scale = Math.min(scaleX, scaleY);

    console.log("Calculated scale:", scale);

    // Function to transform coordinates
    const transformCoord = (coord, min, scale) => (coord - min) * scale + margin;

    // Draw the blueprint
    ctx.beginPath();
    ctx.moveTo(
      transformCoord(blueprint.points[0].x, minX, scale),
      transformCoord(blueprint.points[0].y, minY, scale)
    );
    for (let i = 1; i < blueprint.points.length; i++) {
      ctx.lineTo(
        transformCoord(blueprint.points[i].x, minX, scale),
        transformCoord(blueprint.points[i].y, minY, scale)
      );
    }
    ctx.strokeStyle = '#3498db';
    ctx.lineWidth = 2;
    ctx.stroke();

    // Draw points
    blueprint.points.forEach((point, index) => {
      ctx.beginPath();
      ctx.arc(
        transformCoord(point.x, minX, scale),
        transformCoord(point.y, minY, scale),
        3, 0, 2 * Math.PI
      );
      ctx.fillStyle = index === 0 ? '#2ecc71' : '#e74c3c';
      ctx.fill();
    });

    console.log("Blueprint drawing completed");
  }, []);

  const openModal = useCallback((blueprint) => {
    console.log("Opening modal with blueprint:", blueprint);
    setSelectedBlueprint(blueprint);
    setModalIsOpen(true);
  }, []);

  useEffect(() => {
    if (selectedBlueprint && canvasRef.current) {
      console.log("Effect triggered, drawing blueprint");
      drawBlueprint(selectedBlueprint);
    }
  }, [selectedBlueprint, drawBlueprint]);

  useEffect(() => {
    if (modalIsOpen && selectedBlueprint && canvasRef.current) {
      console.log("Modal is open, redrawing blueprint");
      setTimeout(() => drawBlueprint(selectedBlueprint), 100);
    }
  }, [modalIsOpen, selectedBlueprint, drawBlueprint]);

  const totalPoints = blueprints.reduce((sum, bp) => sum + bp.points.length, 0);

  return (
    <Container>
      <Title><FaDraftingCompass /> Blueprint Viewer</Title>
      <InputGroup>
        <Input
          type="text"
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          placeholder="Enter author name"
          aria-label="Author name"
        />
        <Button onClick={fetchBlueprints}><FaSearch /> Get Blueprints</Button>
      </InputGroup>

      {blueprints.length > 0 && (
        <>
          <h2>{author}'s blueprints:</h2>
          <Table>
            <thead>
              <tr>
                <Th>Blueprint name</Th>
                <Th>Number of points</Th>
                <Th>Action</Th>
              </tr>
            </thead>
            <tbody>
              {blueprints.map((bp) => (
                <tr key={bp.name}>
                  <Td>{bp.name}</Td>
                  <Td>{bp.points.length}</Td>
                  <Td>
                    <Button onClick={() => openModal(bp)}>Open</Button>
                  </Td>
                </tr>
              ))}
            </tbody>
          </Table>
          <TotalPoints>Total user points: {totalPoints}</TotalPoints>
        </>
      )}

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={() => setModalIsOpen(false)}
        contentLabel="Blueprint Modal"
        style={{
          content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            padding: '20px',
            borderRadius: '8px',
            maxWidth: '90%',
            maxHeight: '90%',
          },
        }}
        onAfterOpen={() => {
          console.log("Modal opened, canvas should be available");
          if (selectedBlueprint) drawBlueprint(selectedBlueprint);
        }}
      >
        <ModalContent>
          <h2>{selectedBlueprint?.name}</h2>
          <CanvasContainer>
            <canvas ref={canvasRef} width="400" height="400"></canvas>
          </CanvasContainer>
          <Button onClick={() => setModalIsOpen(false)}>Close</Button>
        </ModalContent>
      </Modal>
    </Container>
  );
}

export default BlueprintViewer;