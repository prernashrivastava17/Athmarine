import React from "react";
import "./index.scss";
import Header from "../header";
import Footer from "../footer";

import sectionOurMission1 from "./../../assets/images/static_website/our_mission_1.jpg";
import sectionOurMission2 from "./../../assets/images/static_website/our_mission_2.jpg";
import sectionOurMission3 from "./../../assets/images/static_website/our_mission_3.jpg";

const AboutUs = () => {
  return (
    <>
      <Header />
      <main>
        <section className="section_our_story">
          <div className="main_wrapper">
            <h1 className="main_quote">
              "The greater the obstacle, the more glory in overcoming it."
            </h1>
            <div className="story_body">
              <h1 className="heading">Our Story</h1>
              <div className="body">
                <p className="para">
                  We are a unique group of experienced professionals from the
                  maritime and technology sectors with a combined experience of
                  more than 135 years. As a team we have among us senior
                  executive level experience with ship owners, ship managers,
                  original equipment manufacturers and pure technology
                  providers. We are enthusiastic about using our combined
                  knowledge about various aspects of the maritime industry and
                  the technology space to make a positive impact on some aspects
                  of the working of our beloved industry.
                </p>
                <p className="para">
                  We believe there exists a great disparity between a service
                  consumer and a service provider and bridging this gap would
                  bringin greater transparencies resulting in greater
                  efficiencies. This solution originates out of this belief and is
                  accelerated with a strong intention to uphold fair service
                  practices, eliminate unfair and illegitimate deals that arise
                  due to lack of information or lack of right attitude, in some
                  cases.
                </p>
                <p className="para">
                  This is our first, but firm step, towards making our industry
                  dealings more transparent, more efficient and even more
                  equalization of fair opportunities and benefits.
                </p>
              </div>
            </div>
          </div>
        </section>

        {/* <section className="section_our_mission">
          <div className="main_wrapper">
            <h1 className="heading">Our Mission</h1>
            <div className="mission_body">
              <div className="card">
                <div className="card_image">
                  <img src={sectionOurMission1} alt="our mission" />
                </div>
                <div className="card_heading">Heading 1</div>
                <div className="card_body">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book.
                </div>
              </div>
              <div className="card">
                <div className="card_image">
                  <img src={sectionOurMission2} alt="our mission" />
                </div>
                <div className="card_heading">Heading 2</div>
                <div className="card_body">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book.
                </div>
              </div>
              <div className="card">
                <div className="card_image">
                  <img src={sectionOurMission3} alt="our mission" />
                </div>
                <div className="card_heading">Heading 3</div>
                <div className="card_body">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book.
                </div>
              </div>
            </div>
          </div>
        </section> */}
      </main>
      <Footer />
    </>
  );
};

export default AboutUs;
