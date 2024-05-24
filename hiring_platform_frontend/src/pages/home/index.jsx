// import React, { useState } from "react";
// import "./Home.scss";
// import Graph from "/home/divum/hiring_platform/trail/hiring-platform-frontend/src/components/Graph/Graph.jsx";
// import Piechart from "/home/divum/hiring_platform/trail/hiring-platform-frontend/src/components/Piechart/index.jsx";
// import NotificationSlideBar from "../../components/NotificationSlider/NotificationSlideBar";

// const Home = () => {
//   const data = [
//     {
//       id: 1,
//       title: "Completed contest",
//       style: { background: "#F7DFFE" },
//       value: "4",
//     },
//     {
//       id: 2,
//       title: "Current Contest",
//       style: { background: "#FFF2C5" },
//       value: "7",
//     },
//     {
//       id: 3,
//       title: "Upcoming Contest",
//       style: { background: "#CEFFD5" },
//       value: "10",
//     },
//   ];

//   const [chartData, setChartData] = useState({
//     labels: [`80%`, `20%`],
//     datasets: [
//       {
//         data: [80, 20],
//         backgroundColor: ["rgb(211, 99, 132)", "#72CC52"],
//         cutout: "0%",
//       },
//     ],
//   });

//   const colorDescriptions = [
//     { color: "rgb(211, 99, 132)", description: "HR Assigned Contest" },
//     { color: "#72CC52", description: "HR  Not Assigned Contest" },
//   ];

//   return (
//     <div className="adminHome">
//       <div>
//         <NotificationSlideBar className={"notificationSidebar"} />
//       </div>
//       <div className="container-row">
//         {data.map((item) => (
//           <div
//             className="completecontest-container"
//             key={item.id}
//             style={item.style}
//           >
//             <p className="value">{item.value}</p>
//             <p className="title">{item.title}</p>
//           </div>
//         ))}
//       </div>
//       <div className="Picto">
//         <h1>Yearly Contest Conducted Chart</h1>
//         <div className="graph-chart">
//           <Graph />
//         </div>
//         <div>
//           <Piechart chartData={chartData} className={"pie-chart"} />
//           <div className="color-descriptions">
//             {colorDescriptions.map((colorDesc, index) => (
//               <div key={index} className="color-description">
//                 <div
//                   className="color-box"
//                   style={{ backgroundColor: colorDesc.color }}
//                 ></div>
//                 <p>{colorDesc.description}</p>
//               </div>
//             ))}
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Home;
