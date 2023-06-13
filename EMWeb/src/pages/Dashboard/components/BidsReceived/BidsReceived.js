import React from "react";
import "./BidsReceived.scss";
import vendorImg from "./../../../../assets/images/dummy.PNG";

function BidsReceived() {
  return (
    <div className="a_bids_received">
      <div className="bredcrum">
        &#x2B9E; Dashboard &#x2B9E; Bids
        <span className="active_page"> &#x2B9E; Add New Request</span>
      </div>
      <div className="main_heading">Bids Received</div>

      <div className="is_bids_received">
        <div className="col">
          <div className="bid_content">
            <span className="content_heading">Vessel :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
          <div className="bid_content">
            <span className="content_heading">Country :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
          <div className="bid_content">
            <span className="content_heading">State :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
          <div className="bid_content">
            <span className="content_heading">Port :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
        </div>
        <div className="col">
          <div className="bid_content">
            <span className="content_heading">ETA :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
          <div className="bid_content">
            <span className="content_heading">ETD :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
          <div className="bid_content">
            <span className="content_heading">Category :</span>
            <span className="content_text">Lorem Ipsum</span>
          </div>
        </div>
        <div className="col">
          <button type="button" className="btn_bid_received">
            Bid Received
          </button>
        </div>
      </div>

      <div className="main_heading">Vendors List</div>
      <div id="accordion">
        <ul>
          <div className="accordion_heading">
            <div className="a_vendors">Vendors</div>
            <div className="a_pricing">Pricing</div>
            <div className="a_description">Description</div>
            <div className="a_engineers">Engineers</div>
            <div className="a_dropdown"></div>
          </div>
          <li>
            <input type="checkbox" checked="false" />
            <i></i>
            <div className="accordion_row">
              <div className="a_vendors">
                <img src={vendorImg} alt="vendor_image" />
              </div>
              <div className="a_pricing">₹ 1,00,000</div>
              <div className="a_description">
                Lorem ipsum dolor sit amet, consecte im ad minim veniam, quis
                nostrud
              </div>
              <div className="a_engineers">3</div>
              <div className="a_dropdown"></div>
            </div>
            <p>
              Lorem Ipsum is simply dummy text of the printing and typesetting
              industry. Lorem Ipsum has been the industry's standard dummy text
              ever since the 1500s, when an unknown printer took a galley of
              type and scrambled it to make a type specimen book. It has
              survived not only five centuries, but also the leap into
              electronic typesetting, remaining essentially unchanged. It was
              popularised in the 1960s with the release of Letraset sheets
              containing Lorem Ipsum passages, and more recently with desktop
              publishing software like Aldus PageMaker including versions of
              Lorem Ipsum.
            </p>
          </li>
          <li>
            <input type="checkbox" checked="false" />
            <i></i>
            <div className="accordion_row">
              <div className="a_vendors">
                <img src={vendorImg} alt="vendor_image" />
              </div>
              <div className="a_pricing">₹ 1,00,000</div>
              <div className="a_description">
                Lorem ipsum dolor sit amet, consecte im ad minim veniam, quis
                nostrud
              </div>
              <div className="a_engineers">3</div>
              <div className="a_dropdown"></div>
            </div>
            <p>
              It is a long established fact that a reader will be distracted by
              the readable content of a page when looking at its layout. The
              point of using Lorem Ipsum is that it has a more-or-less normal
              distribution of letters, as opposed to using 'Content here,
              content here', making it look like readable English. Many desktop
              publishing packages and web page editors now use Lorem Ipsum as
              their default model text, and a search for 'lorem ipsum' will
              uncover many web sites still in their infancy. Various versions
              have evolved over the years, sometimes by accident, sometimes on
              purpose (injected humour and the like).
            </p>
          </li>
          <li>
            <input type="checkbox" checked="false" />
            <i></i>
            <div className="accordion_row">
              <div className="a_vendors">
                <img src={vendorImg} alt="vendor_image" />
              </div>
              <div className="a_pricing">₹ 1,00,000</div>
              <div className="a_description">
                Lorem ipsum dolor sit amet, consecte im ad minim veniam, quis
                nostrud
              </div>
              <div className="a_engineers">3</div>
              <div className="a_dropdown"></div>
            </div>
            <p>
              There are many variations of passages of Lorem Ipsum available,
              but the majority have suffered alteration in some form, by
              injected humour, or randomised words which don't look even
              slightly believable. If you are going to use a passage of Lorem
              Ipsum, you need to be sure there isn't anything embarrassing
              hidden in the middle of text. All the Lorem Ipsum generators on
              the Internet tend to repeat predefined chunks as necessary, making
              this the first true generator on the Internet. It uses a
              dictionary of over 200 Latin words, combined with a handful of
              model sentence structures, to generate Lorem Ipsum which looks
              reasonable. The generated Lorem Ipsum is therefore always free
              from repetition, injected humour, or non-characteristic words etc.
            </p>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default BidsReceived;
