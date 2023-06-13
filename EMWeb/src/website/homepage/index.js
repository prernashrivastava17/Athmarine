import React, { useRef, useEffect, useState } from "react";
import "./index.scss";
import Header from "../header";
import Footer from "./../footer";

const Homepage = () => {
  const videoRef = useRef(null);
  const [playingVideo, setPlayingVideo] = useState(true);

  // useEffect(() => {
  //   if (playingVideo && videoRef) {
  //     videoRef.current.play();
  //   }
  // }, [videoRef, playingVideo]);

  useEffect(() => {
    setTimeout(() => {
      setPlayingVideo(false);
    }, 15000);
  }, []);

  return (
    <>
      <Header />
      <main>
        <section className="section_main">
          <div className="main_wrapper">
            <div className="background_area">
              <div className="content_area">
                <h1>Believe Today, Lead Tomorrow</h1>
                <p>
                  SEARCH
                  <span style={{ position: "relative", top: "-9px" }}>.</span>
                  SOLVE
                  <span style={{ position: "relative", top: "-9px" }}>.</span>
                  SAIL
                </p>

                {/* <button>Get Started</button> */}
              </div>
            </div>
          </div>
          {/* {playingVideo ? (
            <div className="video_wrapper">
              <video
                ref={videoRef}
                src={antMarineVideo}
                width="100%"
                height="400"
                muted
              ></video>
            </div>
          ) : (
            <div className="main_wrapper">
              <div className="background_area">
                <div className="content_area">
                  <h1>Believe Today, Lead Tomorrow</h1>
                  <p>SEARCH . SOLVE . SAIL</p>

                  <button>Get Started</button>
                </div>
              </div>
            </div>
          )} */}
          {/* <div className="video_wrapper">
            <video
              ref={videoRef}
              src={antMarineVideo}
              width="600"
              height="400"
              muted
            ></video>
          </div> */}
        </section>

        <section className="section_whychooseus">
          <div className="main_wrapper">
            <div className="part1">
              <div className="card_part">
                <div className="heading">
                  Why Choose <br /> Us?
                </div>
                <div className="a_body">
                  <p className="heading">ATH for Customers:</p>
                  <p className="body">
                    <ul
                      style={{
                        listStylePosition: "outside",
                        paddingLeft: "18px",
                      }}
                    >
                      <li>One stop solution for all service needs</li>
                      <li>Save time in finding service providers</li>
                      <li>Huge savings in service costs</li>
                      <li>Transparent transactions</li>
                      <li>Eliminate discrepancies</li>
                    </ul>
                  </p>
                  <p className="heading">Service Partners:</p>
                  <p className="body">
                    <ul
                      style={{
                        listStylePosition: "outside",
                        paddingLeft: "18px",
                      }}
                    >
                      <li>Increase engineer utilization rates</li>
                      <li>Maximize profitability</li>
                      <li>
                        Focus only on your core competence - providing service
                        to the vessel
                      </li>
                      <li>Eliminate discrepancies Ensure timely payments</li>
                    </ul>
                  </p>
                </div>
                {/* <div className="button_container">
                  <input
                    type="button"
                    value="Learn More >"
                    className="btn_submit_form"
                  />
                </div> */}
              </div>
            </div>
            <div className="part2">
              <div className="card_part_diff1">
                <div className="card_header">
                  <h2 className="heading">SEARCH</h2>
                  {/* <div className="icon_container">
                    <img
                      src={sectionChooseIcon1}
                      alt="choose icon 1"
                      className="icon"
                    />
                  </div> */}
                </div>
                <div className="card_body">
                  {/* <div className="para">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley.
                  </div> */}
                </div>
              </div>
              <div className="card_part_diff2">
                <div className="card_header">
                  <h2 className="heading">SOLVE </h2>
                  {/* <div className="icon_container">
                    <img
                      src={sectionChooseIcon2}
                      alt="choose icon 2"
                      className="icon"
                    />
                  </div> */}
                </div>
                <div className="card_body">
                  {/* <div className="para">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley.
                  </div> */}
                </div>
              </div>
              <div className="card_part_diff3">
                <div className="card_header">
                  <h2 className="heading">SAIL </h2>
                  {/* <div className="icon_container">
                    <img
                      src={sectionChooseIcon3}
                      alt="choose icon 3"
                      className="icon"
                    />
                  </div> */}
                </div>
                <div className="card_body">
                  {/* <div className="para">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley.
                  </div> */}
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* <section className="section_testimonials">
          <div className="main_wrapper">
            <div className="header">
              <p className="subheading">
                <span>Testimonials</span>
              </p>
              <p className="main_heading">what are they saying</p>
            </div>
            <div className="body">
              <div className="card">
                <div className="part1">
                  <div className="img_area">
                    <img src={testimonial} alt="testimonial" />
                  </div>
                </div>
                <div className="part2">
                  <h1 className="testimonial_name">John Larson</h1>
                  <p className="desg">Freelencer</p>
                  <p className="quote">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley of type and scrambled it to make a
                    type specimen book. It has survived not only five centuries.
                  </p>
                </div>
              </div>

              <div className="card">
                <div className="part1">
                  <div className="img_area">
                    <img src={testimonial} alt="testimonial" />
                  </div>
                </div>
                <div className="part2">
                  <h1 className="testimonial_name">John Larson</h1>
                  <p className="desg">Freelencer</p>
                  <p className="quote">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley of type and scrambled it to make a
                    type specimen book. It has survived not only five centuries.
                  </p>
                </div>
              </div>

              <div className="card">
                <div className="part1">
                  <div className="img_area">
                    <img src={testimonial} alt="testimonial" />
                  </div>
                </div>
                <div className="part2">
                  <h1 className="testimonial_name">John Larson</h1>
                  <p className="desg">Freelencer</p>
                  <p className="quote">
                    Lorem Ipsum is simply dummy text of the printing and
                    typesetting industry. Lorem Ipsum has been the industry's
                    standard dummy text ever since the 1500s, when an unknown
                    printer took a galley of type and scrambled it to make a
                    type specimen book. It has survived not only five centuries.
                  </p>
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

export default Homepage;
