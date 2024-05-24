import React from "react";
import NavBar from "../../components/NavBar/index";
import "./Landingpage.scss";
import Images from "../../components/Image/index";
import assets from "../../assets";
import { TechList } from "../../helpers/ImageList";
import Footer from "../../containers/Footer/index";

function Landingpage() {
  return (
    <div className="Landingpage">
      <NavBar />
      <div className="LandingpageBanner">
        <div className="container">
          <h1>
            <span className="BannerColoredText">
              Powering AI Transformation{" "}
            </span>
            with Intelligent Product Engineering
          </h1>
          <Images src={assets.BannerImage} className="BannerImage" />
        </div>
      </div>

      <div className="AboutSection">
        <h1>About</h1>
        <div className="AboutSectionContainer">
          <p>
            We are a team of 150+ powering some of the biggest brands, OEMS,
            startups on pioneering products and solution initiatives. Since
            inception in 2008, our teams have delivered 1400+ solutions on
            Design, Cloud, Web, Mobile, Blockchain, ML/AI And IoT. Bringing its
            wealth of experience right from product ideation to launch, Divum
            builds stable and scalable products within aggressive timelines
            adopting an agile-based iterative development with focused
            incremental releases.
          </p>
          <br />
          <p>
            We are technology and design aficionados. Our global partnerships
            with OEMs like Google, Amazon, SAP, Microsoft and Samsung help us
            participate in early access programs on upcoming design and
            technology trends and stay ahead of the curve and add value to our
            customer/’s products.
          </p>
          <br />
          <p>
            We are expanding rapidly in global markets with our enterprise
            software development and services, with the motive of bringing
            design thinking and agile development to meet today/’s enterprise
            challenges. At Divum we value innovation, openness, trust, mutual
            respect and encourage continuous learning and risk taking.
          </p>
        </div>
      </div>

      <div className="TechnologySection">
        <div className="TechnologyContainer">
          <h1>Technology</h1>
          <div className="TechIconSection">
            {TechList.map((data, index) => {
              return (
                <div className="TechIcons" key={index}>
                  <Images src={data.image} className="TechList" />
                  <p>{data.text}</p>
                </div>
              );
            })}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default Landingpage;
